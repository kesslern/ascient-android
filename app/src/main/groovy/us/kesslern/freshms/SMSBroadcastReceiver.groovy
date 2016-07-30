package us.kesslern.freshms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import groovy.transform.CompileStatic
import org.springframework.web.client.RestTemplate

@CompileStatic
class SMSBroadcastReceiver extends BroadcastReceiver {

    MainActivity activity
    RestTemplate restTemplate

    SMSBroadcastReceiver() {
        restTemplate = new RestTemplate()
    }

    @Override
    void onReceive(Context context, Intent intent) {
        Log.v('test', 'received')

        Bundle bundle = intent.getExtras()

        if (bundle) {
            Object[] pdus = bundle['pdus'] as Object[]

            for (Object pdu : pdus) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu)
                Log.v('test', message.displayMessageBody)
                activity.update()
            }
        }
    }
}
