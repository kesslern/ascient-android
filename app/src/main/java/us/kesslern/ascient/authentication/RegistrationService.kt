package us.kesslern.ascient.authentication

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.google.gson.Gson
import us.kesslern.ascient.tag

/**
 * Logic for managing registration of the android device with the backend.
 */
object RegistrationService {

    private val TAG = tag(RegistrationService::class)

    data class RegisterRequest(val applicationId: String)

    fun register(applicationId: String, success: (ByteArray) -> Unit, failure: (FuelError) -> Unit) {
        Log.d(TAG, "Initiating registration")

        val request = RegisterRequest(applicationId)

        Fuel.post("http://10.0.2.2:8080/phone/register")
                .body(Gson().toJson(request))
                .response { _, _, result ->
                    result.fold(success, failure)
                }
    }
}
