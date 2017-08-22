package us.kesslern.ascient.activity;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import us.kesslern.ascient.R;
import us.kesslern.ascient.domain.PhoneClient;
import us.kesslern.ascient.receiver.SMSBroadcastReceiver;
import us.kesslern.ascient.service.AndroidIdService;
import us.kesslern.ascient.service.PermissionHandlerService;
import us.kesslern.ascient.util.RestTemplateFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CLIENT_TOKEN = UUID.randomUUID().toString();
    private static final String ENABLED_SWITCH_STATE = "switch_state";

    private int totalReceived = 0;
    private SharedPreferences preferences;
    private IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    private SMSBroadcastReceiver smsReceiver;
    private String androidId;

    @BindView(R.id.totalSent)
    public TextView totalSentTextView;
    @BindView(R.id.uuid)
    public TextView uuidTextView;
    @BindView(R.id.switchSync)
    public Switch syncSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Started main activity.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bindDependencies();
        initializeSwitch();
        totalSentTextView.setText(R.string.no_messages_received);
        PermissionHandlerService.requestPermissions(this);
        uuidTextView.setText(CLIENT_TOKEN + "\n" + androidId);
    }

    public void update() {
        totalReceived = totalReceived++;
        totalSentTextView.setText(
                String.format(getString(R.string.total_received),
                String.valueOf(totalReceived)));
    }

    public void updateBroadcastReceiver(final boolean enabled) {
        if (enabled) {
            registerReceiver(smsReceiver, intentFilter);
        } else {
            unregisterReceiver(smsReceiver);
        }


        preferences.edit().putBoolean(ENABLED_SWITCH_STATE, enabled).apply();
        Log.d(TAG, smsReceiver.getClass().getSimpleName() + " state :" + String.valueOf(enabled));
    }

    @OnClick(R.id.bottomButton)
    public void register() {
        new AsyncTask<Void, Void, PhoneClient>() {
            @Override
            protected PhoneClient doInBackground(Void... params) {
                Log.d(TAG, "Executing regester task...");
                String uri = "http://10.0.2.2:7007/phone/register";
                URI completeUri = UriComponentsBuilder
                        .fromHttpUrl(uri)
                        .queryParam("phoneId", androidId)
                        .queryParam("CLIENT_TOKEN", CLIENT_TOKEN)
                        .build()
                        .toUri();
                return RestTemplateFactory.build().getForObject(completeUri, PhoneClient.class);
            }

        };
    }

    private void initializeSwitch() {
        syncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateBroadcastReceiver(isChecked);
            }

        });

        syncSwitch.setChecked(preferences.getBoolean(ENABLED_SWITCH_STATE, false));
    }

    private void bindDependencies() {
        smsReceiver = new SMSBroadcastReceiver();
        smsReceiver.setActivity(this);
        preferences = getPreferences(MODE_PRIVATE);
        androidId = AndroidIdService.getAndroidId(this);
    }


}
