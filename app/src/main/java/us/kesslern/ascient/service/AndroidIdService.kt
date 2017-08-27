package us.kesslern.ascient.service

import android.app.Activity
import android.provider.Settings

import android.provider.Settings.*

object AndroidIdService {
    fun getAndroidId(activity: Activity): String {
        return Secure.getString(
                activity.applicationContext
                        .contentResolver,
                Secure.ANDROID_ID)
    }
}
