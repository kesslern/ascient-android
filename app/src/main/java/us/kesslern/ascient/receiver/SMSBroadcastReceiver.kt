package us.kesslern.ascient.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log

import us.kesslern.ascient.activity.MainActivity
import android.provider.Telephony
import android.os.Build



class SMSBroadcastReceiver(private val activity: MainActivity) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.v("test", "received")

        val bundle = intent.extras
        val smsMessage: SmsMessage

        smsMessage =
                if (Build.VERSION.SDK_INT >= 19) {
                    val msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                    msgs[0]
                } else {
                    val pdus = bundle.get("pdus") as Array<*>
                    SmsMessage.createFromPdu(pdus[0] as ByteArray)
                }

        Log.v("test", smsMessage.displayMessageBody)
        activity.update()
    }
}
