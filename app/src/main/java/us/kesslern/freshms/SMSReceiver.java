package us.kesslern.freshms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("test", "received");

        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");

            for (Object pdu : pdus) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                Log.v("test", message.getDisplayMessageBody());
            }
        }
    }
}
