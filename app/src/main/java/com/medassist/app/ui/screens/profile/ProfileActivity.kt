package com.medassist.app.ui.screens.profile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.medassist.app.R

/**
 * Profile Activity for MedAssist application
 */
class ProfileActivity : AppCompatActivity() {
    
    private lateinit var backButton: MaterialButton
    private lateinit var saveButton: MaterialButton
    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var ageEditText: TextInputEditText
    
    companion object {
        private const val TAG = "MedAssistProfileActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        
        Log.d(TAG, "ProfileActivity created")
        
        setupViews()
        setupClickListeners()
        loadUserData()
        
        Toast.makeText(this, "Profile loaded!", Toast.LENGTH_SHORT).show()
    }
    
    private fun setupViews() {
        backButton = findViewById(R.id.backButton)
        saveButton = findViewById(R.id.saveButton)
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        ageEditText = findViewById(R.id.ageEditText)
        
        Log.d(TAG, "Views setup complete")
    }
    
    private fun setupClickListeners() {
        backButton.setOnClickListener {
            Log.d(TAG, "Back button clicked")
            Toast.makeText(this, "Returning to home...", Toast.LENGTH_SHORT).show()
            finish()
        }
        
        saveButton.setOnClickListener {
            Log.d(TAG, "Save button clicked")
            saveUserData()
        }
        
        Log.d(TAG, "Click listeners setup complete")
    }
    
    private fun loadUserData() {
        val userName = intent.getStringExtra("USER_NAME") ?: "Guest"
        
        // Set default values based on user type
        if (userName == "Guest") {
            nameEditText.setText("Guest User")
            emailEditText.setText("guest@medassist.com")
            phoneEditText.setText("+1 (555) 123-4567")
            ageEditText.setText("25")
        } else {
            nameEditText.setText("Google User")
            emailEditText.setText("user@gmail.com")
            phoneEditText.setText("+1 (555) 987-6543")
            ageEditText.setText("30")
        }
    }
    
    private fun saveUserData() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val age = ageEditText.text.toString()
        
        if (name.isBlank() || email.isBlank() || phone.isBlank() || age.isBlank()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Simulate saving data
        Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_LONG).show()
        Log.d(TAG, "Profile saved: $name, $email, $phone, $age")
    }
}

