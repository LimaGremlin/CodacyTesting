package com.wayapaychat.ng.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wayapaychat.core.utils.Constants.PILOT_ACTION
import com.wayapaychat.core.utils.Constants.PILOT_EXTRA_KEY
import com.wayapaychat.core.utils.Constants.RIDER_ACTION
import com.wayapaychat.core.utils.Constants.RIDER_EXTRA_KEY
import com.wayapaychat.core.utils.tripleReplace
import com.wayapaychat.ng.R
import com.wayapaychat.ng.presentation.splash.SplashActivity
import org.json.JSONObject

class WayaAppFirebaseMessagingService : FirebaseMessagingService() {
//    @Inject lateinit var updateFirebaseTokenUseCase: UpdateFirebaseTokenUseCase

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val intentToBeSent = Intent()
        p0.data.isNotEmpty().let {
            val firstPattern = "\\"
            val secondPattern = "}\""
            val thirdPattern = "\"{"
            val firstReplacement = ""
            val secondReplacement = "}"
            val thirdReplacement = "{"
            val payload = JSONObject(p0.data as Map<*, *>).tripleReplace(
                firstPattern,
                firstReplacement,
                secondPattern,
                secondReplacement,
                thirdPattern,
                thirdReplacement
            )
            /*val payload = JSONObject(p0.data).replace(
                firstPattern,
                firstReplacement,
                secondPattern,
                secondReplacement,
                thirdPattern,
                thirdReplacement
            )*/
            when (payload["type"]) {
                "rideCreated" -> {
                    intentToBeSent.apply {
                        action = PILOT_ACTION
                        putExtra(PILOT_EXTRA_KEY, payload.toString())
                    }
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intentToBeSent)
                    verifyBodyExist(payload)
                }

                "rideAccepted" -> {
                }

                "rideCancelled" -> {
                    val rideCancelledPayload = JSONObject()
                    rideCancelledPayload.put("status", "rideCancelled")

                    intentToBeSent.apply {
                        action = PILOT_ACTION
                        putExtra(PILOT_EXTRA_KEY, rideCancelledPayload.toString())
                    }
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intentToBeSent)
                    handleRideCancelled(rideCancelledPayload)
                }

                "ridePickedUp" -> {
                    val payloadResource = payload.getJSONObject("resource")
                    val ridePickedUpPayload = payloadResource.getJSONObject("ride")
                    ridePickedUpPayload.put("pilotDetails", payloadResource.getJSONObject("user"))
                    ridePickedUpPayload.put("status", "ridePickedUp")

                    sendRiderLocalBroadcast(ridePickedUpPayload.toString())
                    verifyBodyExist(payload)
                }

                "rideUpdate" -> {

                    sendRiderLocalBroadcast(payload.getString("resource"))

                }
                "pendingPickup" -> {
                    val resource = payload.getJSONObject("resource")
                    resource.put("status", "pilotArrived")

                    sendRiderLocalBroadcast(resource.toString())
                    verifyBodyExist(payload)
                }

                "rideDroppedOff" -> {
                    val resource = payload.getJSONObject("resource")
                    resource.put("status", "rideDroppedOff")

                    sendRiderLocalBroadcast(resource.toString())
                    verifyBodyExist(payload)
                }

                "moneyOwed", "newAppNotification", "possibleExcuseRequest", "reportAccepted" -> {
                    verifyBodyExist(payload)
                }
                else -> {
                    if (payload.has("resource")) {
                        payload.getJSONObject("resource")?.let { resource ->

                            if (resource.has("type")) {
                                resource.getString("type")?.let {
                                    when (it) {

                                        "ridePickedUp" -> {
                                            resource.put("status", "ridePickedUp")
                                            sendRiderLocalBroadcast(resource.toString())
                                        }

                                        "rideCancelled" -> {
                                            val rideCancelledPayload = JSONObject()
                                            rideCancelledPayload.put("status", "rideCancelled")
                                            handleRideCancelled(rideCancelledPayload)
                                        }

                                    }
                                }

                            }
                        }
                    } else {
                    }
                }

            }
        }
    }

    private fun verifyBodyExist(payload: JSONObject) {
        if (payload.has("body")) {
            val notificationMessage = payload["body"] as String
            sendNotification(notificationMessage)
        }
    }

    private fun handleRideCancelled(payload: JSONObject) {
        sendRiderLocalBroadcast(payload.toString())
        sendNotification(getString(R.string.ride_cancelled_notification_message))
    }

    override fun onNewToken(p0: String) {
//        val parameter = UpdateFirebaseTokenUseCase.Parameter(
//            firebaseToken = p0
//        )
//        updateFirebaseTokenUseCase.executeUseCaseAndPerformAction(parameter, {
//            BaseAppLog.e("SuperAppLog", p0)
//        }, {
//            BaseAppLog.e("SuperAppLog", it.message!!)
//        })
    }

    private fun sendRiderLocalBroadcast(payloadString: String) {

        val intentToBeSent = Intent()
        intentToBeSent.apply {
            action = RIDER_ACTION
            putExtra(RIDER_EXTRA_KEY, payloadString)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentToBeSent)

    }

    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0
            /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_gokada_logo)
            .setContentTitle(getString(R.string.fcm_message))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

//        val notificationManager = ContextCompat.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationManager =
            getSystemService(this, NotificationManager::class.java) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.default_chanel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(
            0
            /* ID of notification */, notificationBuilder.build()
        )
    }

}
