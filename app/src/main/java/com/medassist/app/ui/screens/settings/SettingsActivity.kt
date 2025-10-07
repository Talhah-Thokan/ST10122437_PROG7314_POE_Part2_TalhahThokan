package com.medassist.app.ui.screens.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.medassist.app.ui.screens.auth.LoginActivity
import com.medassist.app.R
import com.medassist.app.data.firebase.FirestoreDataSeederActivity
import com.medassist.app.utils.PreferenceManager

/**
 * Settings Activity - User preferences and app configuration
 */
class SettingsActivity : AppCompatActivity() {
    
    private lateinit var backButton: MaterialButton
    private lateinit var logoutButton: MaterialButton
    private lateinit var seedDatabaseButton: MaterialButton
    private lateinit var darkThemeSwitch: SwitchMaterial
    private lateinit var notificationsSwitch: SwitchMaterial
    private lateinit var languageSpinner: MaterialAutoCompleteTextView
    private lateinit var preferenceManager: PreferenceManager
    
    companion object {
        private const val TAG = "SettingsActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        preferenceManager = PreferenceManager(this)
        
        setupViews()
        setupClickListeners()
        loadCurrentSettings()
        
        Log.d(TAG, "SettingsActivity created")
    }
    
    private fun setupViews() {
        backButton = findViewById(R.id.backButton)
        logoutButton = findViewById(R.id.logoutButton)
        seedDatabaseButton = findViewById(R.id.seedDatabaseButton)
        darkThemeSwitch = findViewById(R.id.darkThemeSwitch)
        notificationsSwitch = findViewById(R.id.notificationsSwitch)
        languageSpinner = findViewById(R.id.languageSpinner)
    }
    
    private fun setupClickListeners() {
        backButton.setOnClickListener {
            finish()
        }
        
        seedDatabaseButton.setOnClickListener {
            val intent = Intent(this, FirestoreDataSeederActivity::class.java)
            startActivity(intent)
        }
        
        logoutButton.setOnClickListener {
            handleLogout()
        }
        
        darkThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferenceManager.setDarkThemeEnabled(isChecked)
            applyTheme()
            Toast.makeText(this, "Theme preference saved", Toast.LENGTH_SHORT).show()
        }
        
        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferenceManager.setNotificationsEnabled(isChecked)
            Toast.makeText(this, "Notification preference saved", Toast.LENGTH_SHORT).show()
        }
        
        languageSpinner.setOnItemClickListener { _, _, position, _ ->
            val languages = arrayOf("English", "Spanish", "French", "German")
            val selectedLanguage = languages[position].lowercase()
            preferenceManager.setLanguage(selectedLanguage)
            Toast.makeText(this, "Language preference saved", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun loadCurrentSettings() {
        // Load current theme setting
        darkThemeSwitch.isChecked = preferenceManager.isDarkThemeEnabled()
        
        // Load current notification setting
        notificationsSwitch.isChecked = preferenceManager.areNotificationsEnabled()
        
        // Load current language setting
        val currentLanguage = preferenceManager.getLanguage()
        val languages = arrayOf("English", "Spanish", "French", "German")
        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, languages)
        languageSpinner.setAdapter(adapter)
        languageSpinner.setText(languages.firstOrNull { it.lowercase() == currentLanguage } ?: "English")
        
        // Set user info
        val userName = preferenceManager.getUserName()
        val userEmail = preferenceManager.getUserEmail()
        
        findViewById<android.widget.TextView>(R.id.userNameText).text = userName
        findViewById<android.widget.TextView>(R.id.userEmailText).text = userEmail
    }
    
    private fun applyTheme() {
        val isDarkTheme = preferenceManager.isDarkThemeEnabled()
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES 
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
    
    private fun handleLogout() {
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
        
        // Clear user data
        preferenceManager.logout()
        
        // Navigate to login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
