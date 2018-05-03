package us.kesslern.ascient

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import java.nio.charset.Charset

/**
 * Logic for managing registration of the android device with the backend.
 */
object ApiService {

    private val TAG = tag(ApiService::class)

    fun login(username: String, password: String, success: (String) -> Unit, failure: (FuelError) -> Unit) {

        Log.d(TAG, "Logging in...")

        Fuel.post("http://10.0.2.2:8080/api/token")
                .authenticate(username, password)
                .response { _, response, result ->
                    result.fold({ success(response.data.toString(Charset.defaultCharset())) }, {
                        Log.e(TAG, "${it.exception}")
                        failure(it) })
                }
    }
}
