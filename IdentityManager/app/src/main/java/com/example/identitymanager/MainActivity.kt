package com.example.identitymanager

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var identitiesRecyclerView: RecyclerView
    private lateinit var addIdentityFab: FloatingActionButton
    private lateinit var identityAdapter: IdentityAdapter
    private val identitiesList = ArrayList<Identity>()
    private lateinit var enableAccessibilityServiceButton: Button

    private val addIdentityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getParcelableExtra<Identity>("NEW_IDENTITY")?.let { newIdentity ->
                identitiesList.add(newIdentity)
                identityAdapter.notifyItemInserted(identitiesList.size - 1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        identitiesRecyclerView = findViewById(R.id.identitiesRecyclerView)
        addIdentityFab = findViewById(R.id.addIdentityFab)
        enableAccessibilityServiceButton = findViewById(R.id.enableAccessibilityServiceButton)

        identitiesRecyclerView.layoutManager = LinearLayoutManager(this)
        identityAdapter = IdentityAdapter(identitiesList)
        identitiesRecyclerView.adapter = identityAdapter

        addIdentityFab.setOnClickListener {
            val intent = Intent(this, AddIdentityActivity::class.java)
            addIdentityLauncher.launch(intent)
        }

        enableAccessibilityServiceButton.setOnClickListener {
            if (!isAccessibilityServiceEnabled()) {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateAccessibilityButtonState()
    }

    private fun updateAccessibilityButtonState() {
        if (isAccessibilityServiceEnabled()) {
            enableAccessibilityServiceButton.text = getString(R.string.accessibility_service_enabled)
            enableAccessibilityServiceButton.isEnabled = false
        } else {
            enableAccessibilityServiceButton.text = getString(R.string.enable_accessibility_service)
            enableAccessibilityServiceButton.isEnabled = true
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val expectedComponentName = ComponentName(this, MyAccessibilityService::class.java)
        val enabledServicesSetting = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            ?: return false

        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServicesSetting)
        while (colonSplitter.hasNext()) {
            val componentNameString = colonSplitter.next()
            val enabledComponentName = ComponentName.unflattenFromString(componentNameString)
            if (enabledComponentName != null && enabledComponentName == expectedComponentName) {
                return true
            }
        }
        return false
    }
}
