package us.kesslern.freshms.activity

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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import us.kesslern.freshms.R
import us.kesslern.freshms.receiver.SMSBroadcastReceiver
import us.kesslern.freshms.domain.PhoneClient
import us.kesslern.freshms.service.AndroidIdService
import us.kesslern.freshms.service.PermissionHandlerService
import us.kesslern.freshms.util.Fluent

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

        preferences = getPreferences(MODE_PRIVATE)

        androidId = AndroidIdService.getAndroidId(this)
        PermissionHandlerService.requestPermissions(this)

        smsReceiver = new SMSBroadcastReceiver(activity: this)

        initializeSwitch()
        setSentText()
        uuidTextView.text = "${clientToken}\n${androidId}"
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

    @OnClick(R.id.bottomButton)
    void register() {
        Fluent.async {
            String uri = 'http://10.0.2.2:7007/phone/register'
            URI completeUri = UriComponentsBuilder.fromHttpUrl(uri).queryParam("phoneId", androidId).queryParam("clientToken", clientToken).build().toUri()

            RestTemplate restTemplate = new RestTemplate()
            restTemplate.messageConverters.add(new MappingJackson2HttpMessageConverter())
            restTemplate.getForObject(completeUri, PhoneClient)
        }.then {
            uuidTextView.text = it.phoneId
        }
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
