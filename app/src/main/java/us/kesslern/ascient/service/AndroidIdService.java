package us.kesslern.ascient.service;

import android.app.Activity;
import android.provider.Settings;

import static android.provider.Settings.*;

public class AndroidIdService {
    public static String getAndroidId(Activity activity) {
        return Secure.getString(activity
                        .getApplicationContext()
                        .getContentResolver(),
                        Secure.ANDROID_ID);
    }
}
