package com.medassist.app.ui.screens.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.medassist.app.MainActivity
import com.medassist.app.R
import com.medassist.app.data.firebase.FirebaseAuthManager
import com.medassist.app.utils.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Login Activity for MedAssist application
 */
class LoginActivity : AppCompatActivity() {
    
    private lateinit var googleSignInButton: MaterialButton
    private lateinit var guestButton: MaterialButton
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var firebaseAuthManager: FirebaseAuthManager
    
    // Google Sign-In launcher
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        handleGoogleSignInResult(result.resultCode, result.data)
    }
    
    companion object {
        private const val TAG = "MedAssistLoginActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        preferenceManager = PreferenceManager(this)
        firebaseAuthManager = FirebaseAuthManager(this)
        
        Log.d(TAG, "LoginActivity created")
        
        setupViews()
        setupClickListeners()
        
        // Show startup message
        Toast.makeText(this, "Welcome to MedAssist!", Toast.LENGTH_LONG).show()
    }
    
    private fun setupViews() {
        googleSignInButton = findViewById(R.id.googleSignInButton)
        guestButton = findViewById(R.id.guestButton)
        
        Log.d(TAG, "Views setup complete")
    }
    
    private fun setupClickListeners() {
        googleSignInButton.setOnClickListener {
            Log.d(TAG, "Google Sign-In button clicked")
            startGoogleSignIn()
        }
        
        guestButton.setOnClickListener {
            Log.d(TAG, "Guest button clicked")
            handleGuestLogin()
        }
        
        Log.d(TAG, "Click listeners setup complete")
    }
    
    private fun startGoogleSignIn() {
        try {
            val signInIntent = firebaseAuthManager.getSignInIntent()
            googleSignInLauncher.launch(signInIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting Google Sign-In", e)
            Toast.makeText(this, "Google Sign-In not available", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun handleGoogleSignInResult(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    Toast.makeText(this@LoginActivity, "Signing in with Google...", Toast.LENGTH_SHORT).show()
                    
                    val result = firebaseAuthManager.handleSignInResult(data)
                    result.onSuccess { user ->
                        // Save user data to preferences
                        preferenceManager.setUserLoggedIn(true)
                        preferenceManager.setUserName(user.displayName ?: "Google User")
                        preferenceManager.setUserEmail(user.email ?: "")
                        
                        Toast.makeText(this@LoginActivity, "Welcome, ${user.displayName}!", Toast.LENGTH_LONG).show()
                        navigateToMain()
                    }.onFailure { exception ->
                        Log.e(TAG, "Google Sign-In failed", exception)
                        
                        // Show specific error message based on the error type
                        val errorMsg = when {
                            exception.message?.contains("10") == true -> 
                                "Firebase configuration error. Please add SHA-1 fingerprint to Firebase Console."
                            exception.message?.contains("12501") == true ->
                                "Sign-in cancelled by user."
                            exception.message?.contains("7") == true -> 
                                "Network error. Please check your internet connection."
                            else -> 
                                "Sign-in failed: ${exception.message}"
                        }
                        
                        Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Unexpected error during sign-in", e)
                    Toast.makeText(this@LoginActivity, "Sign-in failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            // User cancelled the sign-in
            Log.w(TAG, "User cancelled Google Sign-In")
            Toast.makeText(this, "Google Sign-In cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun handleGuestLogin() {
        Toast.makeText(this, "Continuing as guest...", Toast.LENGTH_SHORT).show()
        
        // Set guest user data
        preferenceManager.setUserLoggedIn(true)
        preferenceManager.setUserName("Guest User")
        preferenceManager.setUserEmail("guest@medassist.com")
        
        navigateToMain()
    }
    
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

