package us.kesslern.ascient.authentication

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.google.gson.Gson

/**
 * Logic for managing registration of the android device with the backend.
 */
object RegistrationService {

    private val TAG = RegistrationService::class.java.simpleName

    data class RegisterRequest(val androidId: String)

    fun register(androidId: String, success: (ByteArray) -> Unit, failure: (FuelError) -> Unit) {
        Log.d(TAG, "Initiating registration")

        val request = RegisterRequest(androidId)

        Fuel.post("http://10.0.2.2:8080/phone/register")
                .body(Gson().toJson(request))
                .response { _, _, result ->
                    result.fold(success, failure)
                }
    }
}
