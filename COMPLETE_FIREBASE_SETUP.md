# Complete Firebase Setup Guide - MedAssist App

## 🔥 Step-by-Step Firebase Configuration

Follow these steps **exactly** to make your app work perfectly with Firebase.

---

## Step 1: Firebase Project Setup (5 minutes)

### 1.1 Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click **"Add project"** or **"Create a project"**
3. Enter project name: `MedAssist` (or any name you prefer)
4. Click **Continue**
5. **Google Analytics**: Choose whether to enable (optional for this app)
6. Click **Create project**
7. Wait for project creation to complete
8. Click **Continue**

### 1.2 Add Android App to Firebase

1. In your Firebase project dashboard, click the **Android icon** (or "Add app" → Android)
2. **Android package name**: `com.medassist.app` ⚠️ **Must match exactly!**
3. **App nickname** (optional): `MedAssist`
4. **Debug signing certificate SHA-1**: (We'll add this in Step 2)
5. Click **Register app**

---

## Step 2: Get SHA-1 Certificate (CRITICAL for Google Sign-In)

### Option A: Using Gradle (Recommended)

```bash
cd /Users/talhahthokan/Desktop/BCAD/2025/Semester\ 2/PROG7314/PROG7314/2025/POE/ST10122437_PROG7314_POE_Part2_TalhahThokan
./gradlew signingReport
```

Look for output like:
```
Variant: debug
Config: debug
Store: /Users/[your-username]/.android/debug.keystore
Alias: AndroidDebugKey
MD5: XX:XX:XX...
SHA1: A1:B2:C3:D4:E5:F6:G7:H8:I9:J0:K1:L2:M3:N4:O5:P6:Q7:R8:S9:T0
SHA-256: XX:XX:XX...
```

**Copy the SHA-1 value** (the long string with colons)

### Option B: Using keytool

```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

**Copy the SHA1 value**

### 2.1 Add SHA-1 to Firebase

1. In Firebase Console, go to **Project Settings** (gear icon)
2. Scroll down to **"Your apps"**
3. Find your Android app
4. Click **"Add fingerprint"**
5. Paste your SHA-1 certificate
6. Click **Save**

---

## Step 3: Download google-services.json

1. In Firebase Console, still in **Project Settings** → **Your apps**
2. Click **"Download google-services.json"**
3. **IMPORTANT**: Save this file to:
   ```
   /Users/talhahthokan/Desktop/BCAD/2025/Semester 2/PROG7314/PROG7314/2025/POE/ST10122437_PROG7314_POE_Part2_TalhahThokan/app/google-services.json
   ```
4. Make sure it's in the `app/` folder (same level as `build.gradle.kts`)

### Verify File Location

```bash
ls -la app/google-services.json
```

You should see the file listed.

---

## Step 4: Enable Firebase Services

### 4.1 Enable Authentication

1. In Firebase Console, click **Authentication** in the left menu
2. Click **Get started**
3. Go to **Sign-in method** tab
4. Click **Google** provider
5. Toggle **Enable** to ON
6. **Project support email**: Enter your email address
7. Click **Save**

### 4.2 Enable Firestore Database

1. In Firebase Console, click **Firestore Database** in the left menu
2. Click **Create database**
3. Choose **Start in test mode** (for development)
4. Click **Next**
5. Select **Cloud Firestore location**: Choose closest to you (e.g., `us-central` or `europe-west`)
6. Click **Enable**
7. Wait for database creation

### 4.3 Set Firestore Security Rules

1. In **Firestore Database**, click **Rules** tab
2. Replace the rules with:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow anyone to read articles and doctors
    match /articles/{document} {
      allow read: if true;
      allow write: if request.auth != null;
    }
    
    match /doctors/{document} {
      allow read: if true;
      allow write: if request.auth != null;
    }
    
    // Users can read/write their own user document
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Users can create bookings and read their own
    match /bookings/{document} {
      allow create: if request.auth != null;
      allow read: if request.auth != null && 
        (resource.data.userId == request.auth.uid);
    }
  }
}
```

3. Click **Publish**

### 4.4 Enable Cloud Messaging (Optional)

1. In Firebase Console, click **Cloud Messaging** in the left menu
2. No additional setup needed - it's automatically enabled

---

## Step 5: Populate Firestore with Sample Data

### 5.1 Add Sample Articles

1. In Firebase Console, go to **Firestore Database**
2. Click **Start collection**
3. Collection ID: `articles`
4. Click **Next**
5. Add the first document:
   - **Document ID**: Click **Auto-ID**
   - Add fields:
     - `title` (string): `"5 Essential Tips for Better Sleep"`
     - `author` (string): `"Dr. Sarah Johnson"`
     - `summary` (string): `"Learn essential techniques to improve your sleep quality and overall health."`
     - `content` (string): `"Sleep is crucial for overall health and well-being. Learn these proven techniques to improve your sleep quality and overall health. This comprehensive guide covers everything from sleep hygiene to relaxation techniques that can help you get the rest you need."`
     - `imageUrl` (string): `"https://via.placeholder.com/300x200/008B8B/FFFFFF?text=Sleep+Tips"`
     - `date` (string): `"2025-01-15"`
6. Click **Save**
7. Repeat for more articles (add 3-5 articles with different content)

### 5.2 Add Sample Doctors

1. Click **Start collection**
2. Collection ID: `doctors`
3. Add the first doctor:
   - **Document ID**: Click **Auto-ID**
   - Add fields:
     - `name` (string): `"Dr. Sarah Johnson"`
     - `specialty` (string): `"Cardiologist"`
     - `rating` (string): `"4.8"`
     - `distance` (string): `"2.1 km away"`
     - `experience` (string): `"15+ years experience"`
     - `price` (string): `"$150 per visit"`
     - `availability` (string): `"Available today"`
     - `searchTerms` (array): Add items: `cardiology`, `heart`, `sarah`, `johnson`
4. Click **Save**
5. Add 4-5 more doctors with different specialties

### Quick Sample Data (Copy-Paste)

**Article 1:**
```
title: Understanding Heart Health
author: Dr. Michael Chen
summary: Your heart is your most important muscle. Discover the key factors that contribute to cardiovascular health.
content: Everything you need to know about maintaining a healthy heart and preventing cardiovascular diseases. This article covers diet, exercise, and lifestyle changes that can significantly improve your heart health.
imageUrl: https://via.placeholder.com/300x200/20B2AA/FFFFFF?text=Heart+Health
date: 2025-01-14
```

**Doctor 2:**
```
name: Dr. Michael Chen
specialty: General Practitioner
rating: 4.9
distance: 0.8 km away
experience: 12+ years experience
price: $120 per visit
availability: Available tomorrow
searchTerms: [general, family, doctor, michael, chen]
```

---

## Step 6: Verify Setup

### 6.1 Check google-services.json Location

```bash
ls -la app/google-services.json
```

Should show the file exists.

### 6.2 Clean and Rebuild

```bash
./gradlew clean
./gradlew assembleDebug
```

Should build successfully without errors.

### 6.3 Check Logs

Look for these in your console output:
- ✅ No "Failed to retrieve Firebase Instance Id" warnings
- ✅ "Google Sign-In configured successfully"
- ✅ Firebase connections established

---

## Step 7: Test the App

### 7.1 Test Google Sign-In

1. **Open the app**
2. **Tap "Sign in with Google"**
3. **Select your Google account**
4. **Grant permissions**
5. **Should see**: "Welcome, [Your Name]!"
6. **Verify**: Check Firebase Console → Authentication → Users (you should see your account)

### 7.2 Test Articles Loading

1. **Navigate to Articles tab**
2. **Should see**: Articles from Firestore
3. **Tap an article**: Opens detail view
4. **Verify**: Check logs for "Loaded X articles from Firebase"

### 7.3 Test Bookings

1. **Navigate to Bookings tab**
2. **Should see**: Doctors from Firestore
3. **Tap "Book Appointment"**
4. **Fill the form**
5. **Submit**
6. **Verify**: Check Firebase Console → Firestore → bookings collection

---

## Step 8: Firestore Security Rules for Production

When ready for production, update rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Only authenticated users can read articles
    match /articles/{document} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.token.admin == true;
    }
    
    match /doctors/{document} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.token.admin == true;
    }
    
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    match /bookings/{document} {
      allow create: if request.auth != null;
      allow read: if request.auth != null && 
        (resource.data.userId == request.auth.uid || 
         request.auth.token.admin == true);
      allow update: if request.auth != null && 
        (resource.data.userId == request.auth.uid || 
         request.auth.token.admin == true);
      allow delete: if request.auth != null && 
        request.auth.token.admin == true;
    }
  }
}
```

---

## 🔍 Troubleshooting

### Issue 1: "Failed to retrieve Firebase Instance Id"

**Solution:**
- Check `google-services.json` is in `app/` folder
- Run `./gradlew clean`
- Rebuild the app
- Make sure package name is exactly `com.medassist.app`

### Issue 2: Google Sign-In Cancelled/Fails (Error 10)

**Solution:**
- Verify SHA-1 fingerprint is added to Firebase
- Download fresh `google-services.json`
- Clean and rebuild
- Check internet connection

### Issue 3: Articles Don't Load

**Solution:**
- Check Firestore rules (should allow read)
- Verify collection name is exactly `articles`
- Check field names match code (title, author, etc.)
- Check Firebase Console → Firestore for data

### Issue 4: "Permission Denied" in Firestore

**Solution:**
- Update Firestore rules to allow read
- Make sure user is authenticated
- Check rules match the operations you're trying

---

## 📱 Expected Behavior After Setup

### ✅ **With Firebase Configured:**

1. **Google Sign-In**:
   - Opens Google account picker
   - User selects account
   - Signs in successfully
   - Saved to Firebase Authentication
   - User profile saved to Firestore

2. **Articles**:
   - Loads from Firestore
   - Displays real data
   - Fallback to mock data if network fails

3. **Doctors**:
   - Loads from Firestore
   - Search functionality works
   - Booking creates Firestore document

4. **Bookings**:
   - Saves to Firestore
   - User can view their bookings
   - Associated with user ID

### ✅ **Console Logs (Success):**

```
D/FirebaseAuthManager: Google Sign-In configured successfully
D/FirebaseRepository: Fetched 5 articles from Firestore
D/FirebaseRepository: Fetched 6 doctors from Firestore
D/FirebaseRepository: Booking created successfully: abc123
```

---

## 🎯 Quick Setup Checklist

- [ ] Firebase project created
- [ ] Android app added to Firebase
- [ ] SHA-1 fingerprint added
- [ ] google-services.json downloaded to `app/` folder
- [ ] Google Sign-In enabled in Authentication
- [ ] Firestore Database created
- [ ] Firestore security rules updated
- [ ] Sample articles added to Firestore
- [ ] Sample doctors added to Firestore
- [ ] App cleaned and rebuilt
- [ ] Google Sign-In tested successfully
- [ ] Articles loading from Firestore
- [ ] Bookings saving to Firestore

---

## 🚀 You're Done!

After completing all steps, your app will:
- ✅ Use **real Firebase Authentication**
- ✅ Load **real data from Firestore**
- ✅ Save **bookings to the cloud**
- ✅ Work **offline with fallbacks**
- ✅ Be **production-ready**

**Estimated setup time: 15-20 minutes**

If you get stuck at any step, check the Troubleshooting section or the logs in Android Studio!

