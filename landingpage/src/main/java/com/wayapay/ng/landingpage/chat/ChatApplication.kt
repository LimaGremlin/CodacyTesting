package com.wayapay.ng.landingpage.chat

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wayapaychat.remote.services.wayagram.CHAT_BASE_URL
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

open class ChatApplication: AppCompatActivity() {
    private lateinit var mSocket: Socket

    fun setUpSocket(userId: String, token: String){
        try{
            val mOptions = IO.Options()
            mOptions.query = "token=$token"
            mOptions.query = "profileId=$userId"
            mSocket = IO.socket(CHAT_BASE_URL, mOptions)
        }catch (e: URISyntaxException){
            Log.d("chat_service", "", e)
        }
    }

    fun getSocket(): Socket {
        return mSocket
    }
}