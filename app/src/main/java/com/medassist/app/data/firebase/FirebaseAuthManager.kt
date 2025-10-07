package com.medassist.app.data.firebase

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

/**
 * Firebase Authentication Manager
 * Handles Google Sign-In flow and authentication state
 */
class FirebaseAuthManager(private val context: Context) {
    
    private lateinit var googleSignInClient: GoogleSignInClient
    private val firebaseRepository = FirebaseRepository()
    
    companion object {
        private const val TAG = "FirebaseAuthManager"
        private const val RC_SIGN_IN = 1001
    }
    
    init {
        setupGoogleSignIn()
    }
    
    private fun setupGoogleSignIn() {
        try {
            // Get the web client ID from google-services.json
            val webClientId = context.getString(com.medassist.app.R.string.default_web_client_id)
            
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build()
            
            googleSignInClient = GoogleSignIn.getClient(context, gso)
            Log.d(TAG, "Google Sign-In configured successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up Google Sign-In", e)
            // Still create a basic client for fallback
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(context, gso)
        }
    }
    
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }
    
    suspend fun handleSignInResult(data: Intent?): Result<FirebaseUser> {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            
            if (account != null) {
                firebaseSignInWithGoogle(account)
            } else {
                Result.failure(Exception("Google Sign-In failed: No account returned"))
            }
        } catch (e: ApiException) {
            Log.e(TAG, "Google Sign-In failed", e)
            when (e.statusCode) {
                10 -> Result.failure(Exception("Developer error - check configuration"))
                7 -> Result.failure(Exception("Network error - check internet connection"))
                else -> Result.failure(Exception("Sign-in failed: ${e.message}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during sign-in", e)
            Result.failure(e)
        }
    }
    
    private suspend fun firebaseSignInWithGoogle(account: GoogleSignInAccount): Result<FirebaseUser> {
        return try {
            Log.d(TAG, "Firebase sign-in with Google: ${account.email}")
            
            val result = firebaseRepository.signInWithGoogle(account.idToken!!)
            result.onSuccess { user ->
                // Save user profile to Firestore
                try {
                    firebaseRepository.saveUserProfile(user)
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to save user profile", e)
                    // Don't fail the sign-in if profile save fails
                }
            }
            
            result
        } catch (e: Exception) {
            Log.e(TAG, "Firebase authentication failed", e)
            Result.failure(e)
        }
    }
    
    fun signOut() {
        try {
            // Sign out from Google
            googleSignInClient.signOut()
            
            // Sign out from Firebase
            firebaseRepository.signOut()
            
            Log.d(TAG, "User signed out successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error during sign out", e)
        }
    }
    
    fun getCurrentUser(): FirebaseUser? = firebaseRepository.getCurrentUser()
    
    fun isUserSignedIn(): Boolean = firebaseRepository.isUserSignedIn()
    
    // Guest login simulation
    suspend fun signInAsGuest(): Result<FirebaseUser> {
        return try {
            // For guest login, we'll create a temporary anonymous user
            // In a real app, you might want to use Firebase Anonymous Auth
            Log.d(TAG, "Guest login initiated")
            
            // For now, return success with current user or null
            // This allows the app to continue without Firebase authentication
            Result.success(getCurrentUser() ?: throw Exception("Guest login not available"))
        } catch (e: Exception) {
            Log.e(TAG, "Guest login failed", e)
            Result.failure(e)
        }
    }
}
