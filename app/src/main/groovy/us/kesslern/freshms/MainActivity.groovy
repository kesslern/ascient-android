package us.kesslern.freshms

import android.Manifest
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import groovy.transform.CompileStatic

@CompileStatic
class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName()
    private static final String ENABLED_SWITCH_STATE = 'switch_state'

    private int totalReceived = 0
    private SharedPreferences preferences
    private IntentFilter intentFilter = new IntentFilter('android.provider.Telephony.SMS_RECEIVED')
    private SMSReceiver smsReceiver

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        contentView = R.layout.activity_main
        preferences = getPreferences(MODE_PRIVATE)

        requestPermissions()

        smsReceiver = new SMSReceiver()
        smsReceiver.activity = this

        initializeSwitch()
        setSentText()
    }

    void update() {
        totalReceived++
        setSentText()
    }

    void enableBroadcastReceiver() {
        registerReceiver(smsReceiver, intentFilter)
        Log.v(TAG, 'Enabled broadcst receiver')
    }

    void disableBroadcastReceiver() {
        unregisterReceiver(smsReceiver)
        Log.v(TAG, 'Disabled broadcst receiver')
    }

    private void setSentText() {
        TextView textView = (TextView) findViewById(R.id.totalSent)
        textView.setText('Total received: ' + totalReceived)
    }

    private void initializeSwitch() {
        Switch mySwitch = findViewById(R.id.switchSync) as Switch
        mySwitch.onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
             void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferences.edit().putBoolean(ENABLED_SWITCH_STATE, true).commit()
                    enableBroadcastReceiver()
                } else {
                    preferences.edit().putBoolean(ENABLED_SWITCH_STATE, false).commit()
                    disableBroadcastReceiver()
                }
            }
        }

        boolean defaultState = preferences.getBoolean(ENABLED_SWITCH_STATE, false)
        mySwitch.setChecked(defaultState);
    }

    private void requestPermissions() {
        if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    [Manifest.permission.RECEIVE_SMS] as String[],
                    12)
        }
    }
}
