package us.kesslern.ascient.authentication

import android.app.Activity

import android.provider.Settings.*

object AndroidIdService {
    fun getAndroidId(activity: Activity): String {
        return Secure.getString(
                activity.applicationContext
                        .contentResolver,
                Secure.ANDROID_ID)
    }
}
