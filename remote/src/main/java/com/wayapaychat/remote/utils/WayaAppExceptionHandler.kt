package com.wayapaychat.remote.utils

import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.*

object WayaAppExceptionHandler {

    fun getErrorFromThrowable(throwable: Throwable): String {
        return when (throwable) {
            is ConnectException -> WayaAppRemoteConstants.CONNECT_EXCEPTION
            is UnknownHostException -> WayaAppRemoteConstants.UNKNOWN_HOST_EXCEPTION
            is SocketTimeoutException -> WayaAppRemoteConstants.SOCKET_TIME_OUT_EXCEPTION
            is UnknownServiceException -> throwable.localizedMessage
            is ProtocolException -> WayaAppRemoteConstants.PROTOCOL_EXCEPTION
            is HttpException -> {
                return when {
                    throwable.code() == 403 -> {
                        val message = throwable.message()
                        return message ?: WayaAppRemoteConstants.FORBIDDEN_EXCEPTION
                    }
                    throwable.code() == 204 -> {
                        val message = throwable.message()
                        return message ?: WayaAppRemoteConstants.FORBIDDEN_EXCEPTION
                    }
                    else -> try {
                        val response = throwable.response()
                        val json = JSONObject(response?.errorBody()?.string())
                        json.getString("message")
                    } catch (e1: JSONException) {
                        WayaAppRemoteConstants.UNKNOWN_NETWORK_EXCEPTION
                    } catch (e1: IOException) {
                        WayaAppRemoteConstants.UNKNOWN_NETWORK_EXCEPTION
                    }
                }
            }
            else -> WayaAppRemoteConstants.UNKNOWN_NETWORK_EXCEPTION
        }
    }
}
