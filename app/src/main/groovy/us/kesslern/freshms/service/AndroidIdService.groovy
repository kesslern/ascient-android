package us.kesslern.freshms.service

import android.app.Activity
import android.provider.Settings;

class AndroidIdService {
    static String getAndroidId(Activity activity) {
        Settings.Secure.getString(
                activity.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID)
    }
}
