package us.kesslern.freshms;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private int totalReceived = 0;
    private IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    private SMSReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        smsReceiver = new SMSReceiver();
        smsReceiver.activity = this;

        initializeSwitch();

        setSentText();
    }

    public void update() {
        totalReceived++;
        setSentText();
    }

    public void enableBroadcastReceiver() {
        registerReceiver(smsReceiver, intentFilter);
        Log.v(TAG, "Enabled broadcst receiver");
    }

    public void disableBroadcastReceiver() {
        unregisterReceiver(smsReceiver);
        Log.v(TAG, "Disabled broadcst receiver");
    }

    private void setSentText() {
        TextView textView = (TextView) findViewById(R.id.totalSent);
        textView.setText("Total received: " + totalReceived);
    }

    private void initializeSwitch() {
        Switch mySwitch = (Switch) findViewById(R.id.switchSync);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableBroadcastReceiver();
                } else {
                    disableBroadcastReceiver();
                }
            }
        });
    }

    private void requestPermissions() {
        PackageManager pm = this.getPackageManager();
        int hasPerm = pm.checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                this.getPackageName());

        if (hasPerm != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    [Manifest.permission.RECEIVE_SMS] as String[],
                    12)
        }
    }
}
