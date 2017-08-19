package us.kesslern.ascient.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import groovy.transform.CompileStatic
import org.springframework.web.client.RestTemplate
import us.kesslern.ascient.activity.MainActivity

class SMSBroadcastReceiver extends BroadcastReceiver {

    MainActivity activity
    RestTemplate restTemplate

    @Override
    void onReceive(Context context, Intent intent) {
        Log.v('test', 'received')

        Bundle bundle = intent.getExtras()

        if (bundle) {
            Object[] pdus = bundle['pdus'] as Object[]

            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu)
                Log.v('test', smsMessage.displayMessageBody)
                activity.update()
            }
        }
    }
}
