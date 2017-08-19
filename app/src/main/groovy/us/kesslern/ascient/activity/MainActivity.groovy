package us.kesslern.ascient.activity

import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.springframework.web.util.UriComponentsBuilder
import us.kesslern.ascient.R
import us.kesslern.ascient.domain.PhoneClient
import us.kesslern.ascient.receiver.SMSBroadcastReceiver
import us.kesslern.ascient.service.AndroidIdService
import us.kesslern.ascient.service.PermissionHandlerService
import us.kesslern.ascient.util.Fluent
import us.kesslern.ascient.util.RestTemplateFactory

class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName()
    private static final String ENABLED_SWITCH_STATE = 'switch_state'

    private int totalReceived = 0
    private SharedPreferences preferences
    private IntentFilter intentFilter = new IntentFilter('android.provider.Telephony.SMS_RECEIVED')
    private SMSBroadcastReceiver smsReceiver
    private String clientToken = UUID.randomUUID()
    private String androidId

    @BindView(R.id.totalSent)
    public TextView totalSentTextView
    @BindView(R.id.uuid)
    public TextView uuidTextView
    @BindView(R.id.switchSync)
    public Switch syncSwitch
    @BindView(R.id.bottomButton)
    public Button bottomButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        bindDependencies()
        initializeSwitch()
        totalSentTextView.text = 'No messages received.'
        PermissionHandlerService.requestPermissions(this)
        uuidTextView.text = "${clientToken}\n${androidId}"
    }

    void update() {
        totalReceived++
        totalSentTextView.text = "Total received: ${totalReceived}"
    }

    void updateBroadcastReceiver(boolean enabled) {
        if (enabled) {
            registerReceiver(smsReceiver, intentFilter)
        } else {
            unregisterReceiver(smsReceiver)
        }

        preferences.edit().putBoolean(ENABLED_SWITCH_STATE, enabled).commit()
        Log.d(TAG, "${smsReceiver.class.getSimpleName()} state :${enabled}")
    }

    @OnClick(R.id.bottomButton)
    void register() {
        Fluent.async {
            String uri = 'http://10.0.2.2:7007/phone/register'
            URI completeUri = UriComponentsBuilder.fromHttpUrl(uri).queryParam("phoneId", androidId).queryParam("clientToken", clientToken).build().toUri()
            RestTemplateFactory.build().getForObject(completeUri, PhoneClient)
        }.then {
            uuidTextView.text = it.phoneId
        }
    }


    private void initializeSwitch() {
        syncSwitch.onCheckedChangeListener = {
            CompoundButton buttonView, boolean isChecked ->
                updateBroadcastReceiver(isChecked)
        }

        syncSwitch.checked = preferences.getBoolean(ENABLED_SWITCH_STATE, false)
    }

    private void bindDependencies() {
        smsReceiver = new SMSBroadcastReceiver(activity: this)
        preferences = getPreferences(MODE_PRIVATE)
        androidId = AndroidIdService.getAndroidId(this)
    }
}
