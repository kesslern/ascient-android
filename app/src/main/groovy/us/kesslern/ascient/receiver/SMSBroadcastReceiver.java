package us.kesslern.ascient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.springframework.web.client.RestTemplate;

import us.kesslern.ascient.activity.MainActivity;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    private MainActivity activity;
    private RestTemplate restTemplate;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("test", "received");

        Bundle bundle = intent.getExtras();

        if (DefaultGroovyMethods.asBoolean(bundle)) {
            Object[] pdus = DefaultGroovyMethods.asType(DefaultGroovyMethods.getAt(bundle, "pdus"), Object[].class);

            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                Log.v("test", smsMessage.getDisplayMessageBody());
                activity.update();
            }
        }
    }

    public MainActivity getActivity() {
        return activity;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
