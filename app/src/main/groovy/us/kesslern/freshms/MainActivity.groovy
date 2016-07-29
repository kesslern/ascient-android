package us.kesslern.freshms

import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife

class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName()
    private static final String ENABLED_SWITCH_STATE = 'switch_state'

    private int totalReceived = 0
    private SharedPreferences preferences
    private IntentFilter intentFilter = new IntentFilter('android.provider.Telephony.SMS_RECEIVED')
    private SMSBroadcastReceiver smsReceiver
    private String clientId = UUID.randomUUID()
    private String androidId

    @BindView(R.id.totalSent)
    public TextView totalSentTextView
    @BindView(R.id.uuid)
    public TextView uuidTextView
    @BindView(R.id.switchSync)
    public Switch syncSwitch

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        preferences = getPreferences(MODE_PRIVATE)

        androidId = AndroidIdService.getAndroidId(this)
        PermissionHandlerService.requestPermissions(this)

        smsReceiver = new SMSBroadcastReceiver()
        smsReceiver.activity = this

        initializeSwitch()
        setSentText()
        uuidTextView.text = "${clientId}\n${androidId}"
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
        totalSentTextView.text = "Total received: ${totalReceived}"
    }

    private void initializeSwitch() {
        syncSwitch.onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
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

        syncSwitch.checked = preferences.getBoolean(ENABLED_SWITCH_STATE, false)
    }
}
