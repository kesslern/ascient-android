package us.kesslern.ascient.authentication

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.google.gson.Gson
import us.kesslern.ascient.activity.MainActivity

/**
 * Created by nathan on 9/6/17.
 */
object RegistrationService {

    private val TAG = RegistrationService::class.java.simpleName

    data class RegisterRequest(val androidId: String)

    fun register(androidId: String): Unit {
        FuelManager.instance.baseHeaders = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json")

        Log.d(TAG, "Initiating registration")

        val request = RegisterRequest(androidId)

        Fuel.post("http://10.0.2.2:8080/phone/register")
                .body(Gson().toJson(request))
                .response { _, _, result ->
                    result.fold({ d ->
                        Log.d(TAG, "Register data: " + d)
                    }, { err ->
                        Log.e(TAG, "Register error: " + err)
                    })
                }
    }
}
