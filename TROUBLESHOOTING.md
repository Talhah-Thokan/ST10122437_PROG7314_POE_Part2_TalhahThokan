# 🔧 Troubleshooting Guide - MedAssist App

## Common Issues & Solutions

---

## 🔴 Issue 1: "Failed to retrieve Firebase Instance Id"

### Symptoms:
```
W/FA: Failed to retrieve Firebase Instance Id
```

### Causes:
- `google-services.json` not in correct location
- `google-services.json` is for wrong project
- Package name mismatch

### Solutions:

**✅ Solution 1: Check File Location**
```bash
ls -la app/google-services.json
```
Should show the file exists in the `app/` folder.

**✅ Solution 2: Verify Package Name**
Open `google-services.json` and check:
```json
{
  "client": [
    {
      "client_info": {
        "android_client_info": {
          "package_name": "com.medassist.app"  // Must match exactly!
        }
      }
    }
  ]
}
```

**✅ Solution 3: Clean & Rebuild**
```bash
./gradlew clean
rm -rf app/build
./gradlew assembleDebug
```

---

## 🔴 Issue 2: Google Sign-In Cancelled (Error 10)

### Symptoms:
```
W/MedAssistLoginActivity: User cancelled Google Sign-In
E/GoogleSignIn: Error code: 10
```

### Causes:
- SHA-1 fingerprint not added to Firebase
- Wrong SHA-1 fingerprint
- `google-services.json` not updated after adding SHA-1

### Solutions:

**✅ Solution 1: Get Correct SHA-1**
```bash
./gradlew signingReport
```
Look for the debug SHA1 certificate.

**✅ Solution 2: Add SHA-1 to Firebase**
1. Firebase Console → Project Settings
2. Your apps → Android app
3. Add fingerprint → Paste SHA-1
4. **Download new google-services.json**
5. Replace old file
6. Clean and rebuild

**✅ Solution 3: Check SHA-1 Format**
Should look like: `A1:B2:C3:D4:E5:F6:G7:H8:I9:J0:K1:L2:M3:N4:O5:P6:Q7:R8:S9:T0`
(20 pairs of hex digits separated by colons)

---

## 🔴 Issue 3: Articles Not Loading from Firestore

### Symptoms:
- Empty articles list
- Shows mock data instead of Firestore data
- Error logs about Firestore

### Causes:
- No data in Firestore
- Firestore rules deny access
- Collection name mismatch
- Network issue

### Solutions:

**✅ Solution 1: Check Firestore Data**
1. Firebase Console → Firestore Database
2. Verify `articles` collection exists
3. Verify documents have correct fields:
   - title (string)
   - author (string)
   - summary (string)
   - content (string)
   - imageUrl (string)
   - date (string)

**✅ Solution 2: Check Firestore Rules**
```javascript
// Should allow reading
match /articles/{document} {
  allow read: if true;
}
```

**✅ Solution 3: Check Collection Name**
In code: `COLLECTION_ARTICLES = "articles"`
In Firestore: Collection must be named exactly `articles` (lowercase, plural)

**✅ Solution 4: Check Logs**
```
D/FirebaseRepository: Fetched X articles from Firestore
```
If you see "Fallback to sample articles", Firestore is not working.

---

## 🔴 Issue 4: Permission Denied in Firestore

### Symptoms:
```
E/FirebaseRepository: Error fetching articles
PERMISSION_DENIED: Missing or insufficient permissions
```

### Causes:
- Firestore rules too restrictive
- User not authenticated when required
- Wrong collection path

### Solutions:

**✅ Solution 1: Use Open Rules (Development)**
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

**✅ Solution 2: Check Authentication**
Make sure user is signed in before accessing protected data.

**✅ Solution 3: Test Rules**
Firebase Console → Firestore → Rules → Rules Playground
Test your read/write operations.

---

## 🔴 Issue 5: App Crashes on Startup

### Symptoms:
- App closes immediately
- Red error in logcat

### Causes:
- Missing dependencies
- Gradle sync issue
- Corrupted build cache

### Solutions:

**✅ Solution 1: Clean Everything**
```bash
./gradlew clean
rm -rf app/build
rm -rf build
rm -rf .gradle
```

**✅ Solution 2: Rebuild**
```bash
./gradlew assembleDebug
```

**✅ Solution 3: Invalidate Caches (Android Studio)**
1. File → Invalidate Caches
2. Check all options
3. Click "Invalidate and Restart"

**✅ Solution 4: Check Logcat**
Look for the actual error:
```bash
adb logcat | grep -E "AndroidRuntime|FATAL"
```

---

## 🔴 Issue 6: Bookings Not Saving

### Symptoms:
- Booking form submits but nothing saved
- No error messages
- Firestore bookings collection empty

### Causes:
- Firestore rules deny write
- User ID not set correctly
- Network issue

### Solutions:

**✅ Solution 1: Check Firestore Rules**
```javascript
match /bookings/{document} {
  allow create: if request.auth != null;  // Or allow create: if true;
}
```

**✅ Solution 2: Check User Authentication**
Make sure user is logged in (not guest mode if rules require auth).

**✅ Solution 3: Check Logs**
```
D/FirebaseRepository: Booking created successfully: [booking_id]
```

**✅ Solution 4: Check Network**
Make sure device/emulator has internet connection.

---

## 🔴 Issue 7: Build Takes Too Long

### Symptoms:
- Gradle build takes 5+ minutes
- Android Studio freezes during build

### Causes:
- First build after clean
- Large dependency downloads
- Insufficient RAM
- Gradle daemon issues

### Solutions:

**✅ Solution 1: Enable Gradle Daemon**
Create/edit `gradle.properties`:
```properties
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m
```

**✅ Solution 2: Clean Gradle Cache**
```bash
./gradlew --stop
rm -rf ~/.gradle/caches
./gradlew assembleDebug
```

**✅ Solution 3: Use Build Cache**
First build will be slow, subsequent builds will be faster.

**✅ Solution 4: Close Other Apps**
Free up RAM by closing unnecessary applications.

---

## 🔴 Issue 8: Emulator Out of Space

### Symptoms:
```
Error: Requested internal only, but not enough space
```

### Causes:
- Emulator storage full
- Too many apps installed

### Solutions:

**✅ Solution 1: Clear Emulator Data**
1. Android Studio → AVD Manager
2. Wipe Data for emulator
3. Restart emulator

**✅ Solution 2: Uninstall Old Apps**
```bash
adb uninstall com.medassist.app
```

**✅ Solution 3: Increase Emulator Storage**
1. AVD Manager → Edit emulator
2. Show Advanced Settings
3. Internal Storage → Increase to 4096 MB
4. SD Card → Increase to 2048 MB

**✅ Solution 4: Use Real Device**
Connect your phone via USB and test on real device.

---

## 🔴 Issue 9: Theme Not Changing

### Symptoms:
- Toggle dark theme in settings
- Theme doesn't change
- App stays in light/dark mode

### Causes:
- App not recreating
- Preference not saving
- Theme not applied

### Solutions:

**✅ Solution 1: Check Preference Save**
```
D/PreferenceManager: Dark theme enabled: true
```

**✅ Solution 2: Restart App**
Close app completely and reopen (theme applies on startup).

**✅ Solution 3: Force Recreate**
Toggle theme and kill app from recent apps.

---

## 🔴 Issue 10: Network Requests Failing

### Symptoms:
- All API calls fail
- No data loading
- Timeout errors

### Causes:
- No internet connection
- Firestore offline mode
- Network security config

### Solutions:

**✅ Solution 1: Check Internet**
Make sure emulator/device has internet access.

**✅ Solution 2: Enable Internet in Emulator**
Settings → Network & Internet → Wi-Fi → Connect

**✅ Solution 3: Check Permissions**
AndroidManifest.xml should have:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

**✅ Solution 4: Test Connection**
```bash
adb shell ping -c 3 google.com
```

---

## 🆘 Emergency Reset

If nothing works, try this complete reset:

```bash
# 1. Stop Gradle
./gradlew --stop

# 2. Clean everything
./gradlew clean
rm -rf app/build
rm -rf build
rm -rf .gradle

# 3. Uninstall app
adb uninstall com.medassist.app

# 4. Rebuild
./gradlew assembleDebug

# 5. Install
./gradlew installDebug
```

---

## 🔍 Debugging Tools

### View Logs
```bash
adb logcat | grep -E "MedAssist|Firebase"
```

### Check Installed App
```bash
adb shell pm list packages | grep medassist
```

### Clear App Data
```bash
adb shell pm clear com.medassist.app
```

### Check APK Size
```bash
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

### Test Internet
```bash
adb shell ping -c 3 8.8.8.8
```

---

## 📞 Getting Help

If you're still stuck:

1. **Check Logs**: Android Studio → Logcat → Filter by "Error" or "Exception"
2. **Read Error Messages**: They usually tell you what's wrong
3. **Google the Error**: Copy exact error message
4. **Check Firebase Status**: [status.firebase.google.com](https://status.firebase.google.com)
5. **Verify Setup**: Re-read `COMPLETE_FIREBASE_SETUP.md`

---

## ✅ Verification Checklist

Use this to verify everything is working:

- [ ] App builds without errors
- [ ] App installs on emulator/device
- [ ] App opens without crashing
- [ ] Can tap "Sign in with Google"
- [ ] Google Sign-In works OR Demo mode activates
- [ ] Home screen loads
- [ ] Articles tab shows data
- [ ] Bookings tab shows doctors
- [ ] Can navigate between tabs
- [ ] Settings page works
- [ ] Theme toggle works
- [ ] Can create a booking
- [ ] Logs show Firebase connections

---

**If all items are checked, your app is working perfectly!** 🎉

