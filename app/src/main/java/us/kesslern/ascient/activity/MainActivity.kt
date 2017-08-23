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
import org.jetbrains.anko.doAsync

import org.springframework.web.util.UriComponentsBuilder

import java.util.UUID

import us.kesslern.ascient.R
import us.kesslern.ascient.domain.PhoneClient
import us.kesslern.ascient.receiver.SMSBroadcastReceiver
import us.kesslern.ascient.service.AndroidIdService
import us.kesslern.ascient.service.PermissionHandlerService
import us.kesslern.ascient.util.RestTemplateFactory
import us.kesslern.ascient.util.bindView

class MainActivity : AppCompatActivity() {

    private var totalReceived = 0
    private var preferences: SharedPreferences? = null
    private val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
    private var smsReceiver: SMSBroadcastReceiver? = null
    private var androidId: String? = null

    private val totalSentTextView: TextView by bindView(R.id.totalSent)
    private val uuidTextView: TextView by bindView(R.id.uuid)
    private val syncSwitch: Switch by bindView(R.id.switchSync)
    private val registerButton: Button by bindView(R.id.bottomButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "Started main activity.")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindDependencies()
        initializeSwitch()
        totalSentTextView.setText(R.string.no_messages_received)
        PermissionHandlerService.requestPermissions(this)
        uuidTextView.text = CLIENT_TOKEN + "\n" + androidId

        registerButton.setOnClickListener { register() }
    }

    fun update() {
        totalReceived = totalReceived++
        totalSentTextView.text = String.format(
                getString(R.string.total_received),
                totalReceived.toString())
    }

    fun updateBroadcastReceiver(enabled: Boolean) {
        if (enabled) {
            registerReceiver(smsReceiver, intentFilter)
        } else {
            unregisterReceiver(smsReceiver)
        }

        preferences!!.edit().putBoolean(ENABLED_SWITCH_STATE, enabled).apply()
        Log.d(TAG, SMSBroadcastReceiver::class.java.simpleName + " state :" + enabled.toString())
    }

    fun register() {
        Log.d(TAG, "Bottom button pressed.")

        doAsync(
                exceptionHandler = {
                    Log.e(TAG, "Error occurred")
                },
                task = {
                    Log.d(TAG, "Executing register task...")
                    val uri = "http://10.0.2.2:7007/phone/register"
                    val completeUri = UriComponentsBuilder
                            .fromHttpUrl(uri)
                            .queryParam("phoneId", androidId)
                            .queryParam("CLIENT_TOKEN", CLIENT_TOKEN)
                            .build()
                            .toUri()
                    RestTemplateFactory.build().getForObject<PhoneClient>(completeUri, PhoneClient::class.java)
                }
        )
    }

    private fun initializeSwitch() {
        syncSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            updateBroadcastReceiver(isChecked)
        }
        syncSwitch.isChecked = preferences!!.getBoolean(ENABLED_SWITCH_STATE, false)
    }

    private fun bindDependencies() {
        smsReceiver = SMSBroadcastReceiver()
        smsReceiver!!.activity = this
        preferences = getPreferences(Context.MODE_PRIVATE)
        androidId = AndroidIdService.getAndroidId(this)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val CLIENT_TOKEN = UUID.randomUUID().toString()
        private val ENABLED_SWITCH_STATE = "switch_state"
    }
}
