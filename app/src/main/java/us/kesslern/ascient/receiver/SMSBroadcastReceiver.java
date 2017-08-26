package us.kesslern.ascient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import us.kesslern.ascient.activity.MainActivity;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    private MainActivity activity;

    public SMSBroadcastReceiver(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("test", "received");

        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[]) bundle.get("pdus");

        for (Object pdu : pdus) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
            Log.v("test", smsMessage.getDisplayMessageBody());
            activity.update();
        }
    }
}
