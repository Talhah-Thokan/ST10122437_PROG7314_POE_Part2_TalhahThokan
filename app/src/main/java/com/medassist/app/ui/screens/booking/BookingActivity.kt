package com.medassist.app.ui.screens.booking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.medassist.app.R
import com.medassist.app.utils.PreferenceManager

/**
 * Booking Activity - Appointment booking form
 */
class BookingActivity : AppCompatActivity() {
    
    private lateinit var backButton: MaterialButton
    private lateinit var bookButton: MaterialButton
    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var reasonEditText: TextInputEditText
    private lateinit var preferenceManager: PreferenceManager
    
    private var doctorName: String = ""
    private var doctorSpecialty: String = ""
    private var doctorId: String = ""
    
    companion object {
        private const val TAG = "BookingActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        
        preferenceManager = PreferenceManager(this)
        
        // Get doctor info from intent
        doctorName = intent.getStringExtra("DOCTOR_NAME") ?: ""
        doctorSpecialty = intent.getStringExtra("DOCTOR_SPECIALTY") ?: ""
        doctorId = intent.getStringExtra("DOCTOR_ID") ?: ""
        
        setupViews()
        setupClickListeners()
        populateUserData()
        
        Log.d(TAG, "BookingActivity created for doctor: $doctorName")
    }
    
    private fun setupViews() {
        backButton = findViewById(R.id.backButton)
        bookButton = findViewById(R.id.bookButton)
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        reasonEditText = findViewById(R.id.reasonEditText)
        
        // Set doctor info
        findViewById<android.widget.TextView>(R.id.doctorNameText).text = doctorName
        findViewById<android.widget.TextView>(R.id.doctorSpecialtyText).text = doctorSpecialty
    }
    
    private fun setupClickListeners() {
        backButton.setOnClickListener {
            finish()
        }
        
        bookButton.setOnClickListener {
            handleBooking()
        }
    }
    
    private fun populateUserData() {
        // Pre-fill with user data
        nameEditText.setText(preferenceManager.getUserName())
        emailEditText.setText(preferenceManager.getUserEmail())
        phoneEditText.setText("+1 (555) 123-4567") // Default phone
    }
    
    private fun handleBooking() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val reason = reasonEditText.text.toString()
        
        // Validate form
        if (name.isBlank() || email.isBlank() || phone.isBlank() || reason.isBlank()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Show loading
        Toast.makeText(this, "Booking appointment...", Toast.LENGTH_SHORT).show()
        
        // TODO: Implement actual API call to /bookings endpoint
        // For now, we'll simulate the booking process
        simulateBooking(name, email, phone, reason)
    }
    
    private fun simulateBooking(name: String, email: String, phone: String, reason: String) {
        // Simulate API call delay
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            // Simulate successful booking
            val bookingId = "BK${System.currentTimeMillis()}"
            val appointmentDate = "2025-01-20"
            val appointmentTime = "10:00 AM"
            
            Log.d(TAG, "Booking created: $bookingId for $doctorName")
            
            // Navigate to confirmation screen
            val intent = Intent(this, BookingConfirmationActivity::class.java)
            intent.putExtra("BOOKING_ID", bookingId)
            intent.putExtra("DOCTOR_NAME", doctorName)
            intent.putExtra("DOCTOR_SPECIALTY", doctorSpecialty)
            intent.putExtra("APPOINTMENT_DATE", appointmentDate)
            intent.putExtra("APPOINTMENT_TIME", appointmentTime)
            intent.putExtra("PATIENT_NAME", name)
            intent.putExtra("PATIENT_EMAIL", email)
            startActivity(intent)
            
            finish()
        }, 2000)
    }
}

