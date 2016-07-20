package us.kesslern.freshms;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int sent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                12);
    }

    public void update(View view) {
        sent++;
        setSentText();
    }

    private void setSentText() {
        TextView textView = (TextView) findViewById(R.id.totalSent);
        textView.setText("Total sent: " + sent);
    }
}
