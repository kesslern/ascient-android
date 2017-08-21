package us.kesslern.ascient.service;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Request and manage permissions.
 */
public class PermissionHandlerService {
    private static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    private static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

    public static void requestPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECEIVE_SMS}, 12);
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, 12);
    }
}
