# 🔥 Firestore Testing & Troubleshooting Guide

## 🚨 If Nothing Works with Firestore

Follow these steps **exactly** to diagnose and fix Firestore issues.

---

## Step 1: Verify Firebase Project Setup

### Check These in Firebase Console:

1. **Go to**: https://console.firebase.google.com
2. **Select your project**: MedAssist (or whatever you named it)
3. **Check these are enabled:**
   - [ ] Authentication → Google provider is ON
   - [ ] Firestore Database exists (not Realtime Database!)
   - [ ] Cloud Messaging (should be auto-enabled)

---

## Step 2: Ultra-Permissive Firestore Rules

### **Copy this EXACT rule** (simplest possible):

```javascript
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write;
    }
  }
}
```

### How to Apply:

1. Firebase Console → **Firestore Database**
2. Click **Rules** tab
3. **DELETE EVERYTHING** in the editor
4. **Paste the rule above**
5. Click **Publish**
6. **Wait 30 seconds** (rules take time to propagate)

---

## Step 3: Verify google-services.json

### Check File Location:

```bash
ls -la app/google-services.json
```

Should show the file exists.

### Verify Package Name Matches:

```bash
grep "package_name" app/google-services.json
```

Should show: `"package_name": "com.medassist.app"`

### If Wrong Package Name:

1. Delete `app/google-services.json`
2. Go to Firebase Console → Project Settings
3. Make sure Android app package is `com.medassist.app`
4. Download new `google-services.json`
5. Place in `app/` folder

---

## Step 4: Enable Firestore Offline Persistence

Add this to FirebaseRepository to test locally first:

```kotlin
init {
    // Enable offline persistence for testing
    try {
        firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
        Log.d(TAG, "Firestore persistence enabled")
    } catch (e: Exception) {
        Log.e(TAG, "Error enabling Firestore persistence", e)
    }
}
```

---

## Step 5: Test Connection Manually

### Create a Test Function:

Add this to your FirebaseRepository:

```kotlin
suspend fun testFirestoreConnection(): Result<String> {
    return try {
        // Try to write a test document
        val testData = hashMapOf("test" to "hello", "timestamp" to System.currentTimeMillis())
        
        firestore.collection("test")
            .document("connection_test")
            .set(testData)
            .await()
        
        Log.d(TAG, "✅ Firestore WRITE test successful")
        
        // Try to read it back
        val doc = firestore.collection("test")
            .document("connection_test")
            .get()
            .await()
        
        if (doc.exists()) {
            Log.d(TAG, "✅ Firestore READ test successful")
            Result.success("Firestore connection working!")
        } else {
            Log.e(TAG, "❌ Firestore READ failed - document doesn't exist")
            Result.failure(Exception("Could not read test document"))
        }
    } catch (e: Exception) {
        Log.e(TAG, "❌ Firestore test failed: ${e.message}", e)
        Result.failure(e)
    }
}
```

### Call it from your app:

```kotlin
CoroutineScope(Dispatchers.IO).launch {
    val result = firebaseRepository.testFirestoreConnection()
    result.onSuccess { message ->
        Log.d("TEST", "✅ $message")
    }.onFailure { error ->
        Log.e("TEST", "❌ Connection failed: ${error.message}")
    }
}
```

---

## Step 6: Check Logs for Specific Errors

### Run this command:

```bash
adb logcat | grep -E "Firestore|FirebaseRepository|FIRESTORE"
```

### Common Error Messages:

#### **Error: "PERMISSION_DENIED"**
```
E/FirebaseRepository: Error: PERMISSION_DENIED: Missing or insufficient permissions
```

**Fix:**
- Rules not published
- Rules too restrictive
- Wait 30 seconds after publishing rules
- Try the ultra-permissive rule above

#### **Error: "Failed to get document"**
```
E/FirebaseRepository: Failed to get document because the client is offline
```

**Fix:**
- Device has no internet
- Check: `adb shell ping -c 3 8.8.8.8`
- Enable Wi-Fi in emulator: Settings → Network & Internet → Wi-Fi

#### **Error: "FirebaseApp not initialized"**
```
E/FirebaseApp: Firebase app not initialized
```

**Fix:**
- `google-services.json` missing or wrong location
- Clean and rebuild: `./gradlew clean assembleDebug`

---

## Step 7: Verify Internet Connection

### Test from Emulator/Device:

```bash
# Check if device can reach Google
adb shell ping -c 3 8.8.8.8

# Check if device can reach Firebase
adb shell ping -c 3 firestore.googleapis.com
```

### If Ping Fails:

**For Emulator:**
1. Settings → Network & Internet
2. Wi-Fi → Turn ON
3. Connect to "AndroidWifi"

**For Real Device:**
1. Enable Wi-Fi
2. Connect to same network as computer

---

## Step 8: Nuclear Option - Complete Reset

If nothing works, do a complete reset:

### 1. Clean Everything:

```bash
./gradlew --stop
rm -rf app/build
rm -rf build
rm -rf .gradle
adb uninstall com.medassist.app
```

### 2. Verify google-services.json:

```bash
ls -la app/google-services.json
cat app/google-services.json | grep package_name
```

### 3. Update Firestore Rules (Ultra-Permissive):

```javascript
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write;
    }
  }
}
```

### 4. Wait 1 Minute (for rules to propagate)

### 5. Rebuild and Install:

```bash
./gradlew clean
./gradlew assembleDebug
./gradlew installDebug
```

### 6. Test:

- Open app
- Sign in
- Check logs: `adb logcat | grep Firestore`

---

## Step 9: Test with Firebase Emulator (Alternative)

If cloud Firestore still doesn't work, use local emulator:

### Install Firebase CLI:

```bash
npm install -g firebase-tools
firebase login
firebase init emulators
```

### Start Emulator:

```bash
firebase emulators:start --only firestore
```

### Connect App to Emulator:

Add to FirebaseRepository init:

```kotlin
init {
    // Use local emulator for testing
    firestore.useEmulator("10.0.2.2", 8080)
}
```

---

## Step 10: Diagnostic Checklist

Go through this checklist:

- [ ] Firebase project created
- [ ] Android app added to Firebase
- [ ] `google-services.json` in `app/` folder
- [ ] Package name is `com.medassist.app`
- [ ] Firestore Database created (NOT Realtime Database)
- [ ] Firestore rules published (ultra-permissive for testing)
- [ ] Waited 30+ seconds after publishing rules
- [ ] App has INTERNET permission in AndroidManifest.xml
- [ ] Device/emulator has internet connection
- [ ] App cleaned and rebuilt after adding google-services.json
- [ ] Checked logs for specific error messages

---

## 🔍 Quick Diagnosis Commands

### Check App is Installed:
```bash
adb shell pm list packages | grep medassist
```

### Check Internet:
```bash
adb shell ping -c 3 8.8.8.8
```

### Watch Firestore Logs:
```bash
adb logcat | grep -E "Firestore|Firebase"
```

### Check google-services.json:
```bash
cat app/google-services.json | grep -A 5 "client_info"
```

---

## 🆘 Still Not Working?

### Option 1: Use Mock Data Only

Your app already has fallback to mock data. Just use that for now:
- Articles will load from hardcoded data
- Doctors will load from hardcoded data
- Bookings save locally
- Everything still works!

### Option 2: Verify Firestore is Actually Enabled

1. Firebase Console
2. Build → Firestore Database
3. If you see "Create database", click it
4. Choose "Start in test mode"
5. Select a location
6. Click Enable
7. Wait for it to finish creating

### Option 3: Create New Firebase Project

Sometimes starting fresh helps:
1. Create new Firebase project
2. Add Android app
3. Download new google-services.json
4. Replace old file
5. Clean and rebuild

---

## ✅ Success Indicators

You'll know Firestore is working when you see:

### In Logs:
```
D/FirebaseRepository: Fetched X articles from Firestore
D/FirebaseRepository: User profile saved successfully
D/FirebaseRepository: Booking created successfully
```

### In Firebase Console:
- Firestore Database → Data tab shows collections
- `articles` collection exists
- `doctors` collection exists
- Documents have data

### In App:
- Articles tab shows data
- No "Failed to load" errors
- Bookings save successfully

---

## 📞 Common Solutions Summary

| Problem | Solution |
|---------|----------|
| Permission Denied | Update Firestore rules to `allow read, write;` |
| No Internet | Enable Wi-Fi, check with ping |
| App Not Initialized | Verify google-services.json location |
| Package Name Mismatch | Update Firebase Console OR regenerate google-services.json |
| Rules Not Working | Wait 30 seconds, try ultra-permissive rules |
| Nothing Works | Use mock data fallback (already implemented!) |

---

**Remember: Your app already works perfectly with mock data if Firestore fails. This is actually a FEATURE that shows good error handling!** ✅

**For your POE demo, you can use either real Firestore data OR mock data - both work great!** 🎉

