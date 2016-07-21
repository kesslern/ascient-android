package us.kesslern.freshms;

import android.Manifest;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private int sent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                12);

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

    public void update(View view) {
        sent++;
        setSentText();
    }

    public void enableBroadcastReceiver() {
        ComponentName receiver = new ComponentName(this, SMSReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Log.v(TAG, "Enabled broadcast receiver");
    }

    public void disableBroadcastReceiver() {
        ComponentName receiver = new ComponentName(this, SMSReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Log.v(TAG, "Disabled broadcst receiver");
    }

    private void setSentText() {
        TextView textView = (TextView) findViewById(R.id.totalSent);
        textView.setText("Total sent: " + sent);
    }
}
