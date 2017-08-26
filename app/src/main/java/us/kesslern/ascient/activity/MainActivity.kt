package us.kesslern.ascient.activity

import android.content.Context
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import com.github.kittinunf.fuel.Fuel
import us.kesslern.ascient.R
import us.kesslern.ascient.receiver.SMSBroadcastReceiver
import us.kesslern.ascient.service.AndroidIdService
import us.kesslern.ascient.service.PermissionHandlerService
import us.kesslern.ascient.util.bindView
import java.util.*

class MainActivity : AppCompatActivity() {

    private var totalReceived = 0
    private val preferences: SharedPreferences by lazy { getPreferences(Context.MODE_PRIVATE) }
    private val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
    private val smsReceiver = SMSBroadcastReceiver(this)
    private val androidId: String by lazy { AndroidIdService.getAndroidId(this) }

    private val totalSentTextView: TextView by bindView(R.id.totalSent)
    private val uuidTextView: TextView by bindView(R.id.uuid)
    private val syncSwitch: Switch by bindView(R.id.switchSync)
    private val registerButton: Button by bindView(R.id.bottomButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PermissionHandlerService.requestPermissions(this)

        totalSentTextView.setText(R.string.no_messages_received)
        uuidTextView.text = "$CLIENT_TOKEN\n$androidId"

        registerButton.setOnClickListener { register() }

        syncSwitch.setOnCheckedChangeListener { _, isChecked ->
            updateBroadcastReceiver(isChecked)
        }
        syncSwitch.isChecked = preferences.getBoolean(ENABLED_SWITCH_STATE, false)
    }

    fun update() {
        totalReceived = totalReceived++
        totalSentTextView.text = String.format(
                getString(R.string.total_received),
                totalReceived.toString())
    }

    private fun updateBroadcastReceiver(enabled: Boolean) {
        if (enabled) {
            registerReceiver(smsReceiver, intentFilter)
        } else {
            unregisterReceiver(smsReceiver)
        }

        preferences.edit().putBoolean(ENABLED_SWITCH_STATE, enabled).apply()
        Log.d(TAG, SMSBroadcastReceiver::class.java.simpleName + " state :" + enabled.toString())
    }

    fun register() {
        Log.d(TAG, "Bottom button pressed.")

        Fuel.post("http://10.0.2.2:7007/phone/register").response { _, _, result ->
            result.fold({ d ->
                Log.d(TAG, "Register data: " + d)
            }, { err ->
                Log.e(TAG, "Register error: " + err)
            })
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val CLIENT_TOKEN = UUID.randomUUID().toString()
        private val ENABLED_SWITCH_STATE = "switch_state"
    }
}
