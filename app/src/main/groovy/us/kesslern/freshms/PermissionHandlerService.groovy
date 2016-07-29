package us.kesslern.freshms

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
/**
 * Request and manage permissions.
 */
public class PermissionHandlerService {

    private static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS
    private static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED

    static void requestPermissions(Activity activity) {
        if (activity.checkSelfPermission(RECEIVE_SMS) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    [Manifest.permission.RECEIVE_SMS] as String[],
                    12)
        }
    }
}
