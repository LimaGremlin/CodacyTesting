package com.wayapaychat.core.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wayapaychat.core.listeners.SingleArgClickListener

class SMSBroadcastReceiver constructor(
    private val smsReceivedListener: SingleArgClickListener<String>
): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

    }
}
