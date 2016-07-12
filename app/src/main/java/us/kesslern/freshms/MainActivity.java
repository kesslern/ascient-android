package us.kesslern.freshms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int sent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
