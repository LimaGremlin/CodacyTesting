package com.wayapay.ng.landingpage.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permissions{
    const val WRITE_EXTERNAL_STORAGE = 201
    const val CAMERA_REQUEST_CODE = 202
}

fun checkReadWritePermission(activity: Context): Boolean {
    val currentAPIVersion = Build.VERSION.SDK_INT
    if (currentAPIVersion >= Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity as Activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                val alertBuilder = AlertDialog.Builder(activity)
                alertBuilder.setCancelable(true)
                alertBuilder.setTitle("Permission necessary")
                alertBuilder.setMessage("External storage permission is necessary")
                alertBuilder.setPositiveButton(
                    android.R.string.yes
                ) { _, _ ->
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        Permissions.WRITE_EXTERNAL_STORAGE
                    )
                }
                val alert = alertBuilder.create()
                alert.show()
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Permissions.WRITE_EXTERNAL_STORAGE
                )
            }
            return false
        } else {
            return true
        }
    } else {
        return true
    }
}