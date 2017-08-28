package us.kesslern.ascient.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

/**
 * Request and manage permissions.
 */
object PermissionHandlerService {

    fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECEIVE_SMS), 12)
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.INTERNET), 12)
    }
}
