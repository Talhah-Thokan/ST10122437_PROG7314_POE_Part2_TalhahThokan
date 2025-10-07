# Firebase Setup Guide for MedAssist

## 🔥 Firebase Configuration Steps

### 1. Firebase Project Setup

1. **Create Firebase Project**:
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Click "Create a project"
   - Name: `medassist-app`
   - Enable Google Analytics (optional)

2. **Add Android App**:
   - Click "Add app" → Android
   - Package name: `com.medassist.app`
   - App nickname: `MedAssist`
   - SHA-1: (optional for development)

### 2. Download Configuration File

1. **Download `google-services.json`**:
   - Click "Download google-services.json"
   - Place it in `app/` directory (same level as `build.gradle.kts`)

2. **Update Web Client ID**:
   - Open `google-services.json`
   - Find `client_id` under `oauth_client` with `client_type: 3`
   - Copy the client ID
   - Update `app/src/main/res/values/strings.xml`:
   ```xml
   <string name="default_web_client_id">YOUR_ACTUAL_WEB_CLIENT_ID</string>
   ```

### 3. Enable Firebase Services

#### Authentication
1. Go to **Authentication** → **Sign-in method**
2. Enable **Google** provider
3. Add your project's support email
4. Save

#### Firestore Database
1. Go to **Firestore Database**
2. Click **Create database**
3. Choose **Start in test mode** (for development)
4. Select location (choose closest to your users)

#### Cloud Messaging
1. Go to **Cloud Messaging**
2. No additional setup needed for basic functionality

### 4. Firestore Database Structure

Create the following collections in Firestore:

#### Articles Collection (`articles`)
```javascript
{
  title: "string",
  author: "string", 
  summary: "string",
  content: "string",
  imageUrl: "string",
  date: "string"
}
```

#### Doctors Collection (`doctors`)
```javascript
{
  name: "string",
  specialty: "string",
  rating: "string",
  distance: "string",
  experience: "string",
  price: "string",
  availability: "string",
  searchTerms: ["array", "of", "strings"]
}
```

#### Bookings Collection (`bookings`)
```javascript
{
  doctorId: "string",
  patientName: "string",
  patientEmail: "string",
  appointmentDate: "string",
  appointmentTime: "string",
  reason: "string",
  userId: "string",
  status: "string",
  createdAt: "number"
}
```

#### Users Collection (`users`)
```javascript
{
  uid: "string",
  name: "string",
  email: "string",
  photoUrl: "string",
  createdAt: "number",
  lastLogin: "number"
}
```

### 5. Sample Data Population

The app includes a `FirestoreDataSeeder` class that can populate your database with sample data. To use it:

1. **Manual Seeding** (for development):
   - Add a button in your app to trigger seeding
   - Or call it programmatically during development

2. **Firestore Console** (recommended):
   - Use the Firestore console to manually add sample documents
   - Copy the structure from `FirestoreDataSeeder.kt`

### 6. Security Rules

Update Firestore security rules for production:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can read/write their own user document
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Anyone can read articles and doctors
    match /articles/{document} {
      allow read: if true;
    }
    
    match /doctors/{document} {
      allow read: if true;
    }
    
    // Users can create bookings and read their own bookings
    match /bookings/{document} {
      allow create: if request.auth != null;
      allow read: if request.auth != null && 
        (resource.data.userId == request.auth.uid || 
         request.auth.token.admin == true);
    }
  }
}
```

### 7. Testing Firebase Integration

1. **Build and Run**:
   ```bash
   ./gradlew clean build
   ./gradlew installDebug
   ```

2. **Test Google Sign-In**:
   - Tap "Sign in with Google"
   - Complete the OAuth flow
   - Check Firebase Console → Authentication for new user

3. **Test Firestore**:
   - Navigate to Articles or Bookings tabs
   - Data should load from Firestore
   - Check Firebase Console → Firestore for data

### 8. Production Considerations

1. **Security Rules**: Update Firestore rules for production
2. **Authentication**: Configure authorized domains
3. **Analytics**: Enable Firebase Analytics if desired
4. **Crashlytics**: Add Firebase Crashlytics for crash reporting
5. **Performance**: Monitor with Firebase Performance Monitoring

### 9. Troubleshooting

#### Common Issues:

1. **Google Sign-In Fails**:
   - Check SHA-1 fingerprint in Firebase Console
   - Verify `google-services.json` is in correct location
   - Ensure Web Client ID is correct in `strings.xml`

2. **Firestore Permission Denied**:
   - Check security rules
   - Ensure user is authenticated
   - Verify collection/document names match code

3. **Build Errors**:
   - Clean and rebuild: `./gradlew clean build`
   - Check Firebase SDK versions in `build.gradle.kts`
   - Ensure all dependencies are compatible

### 10. Next Steps

Once Firebase is configured:

1. **Test all features** with real Firebase data
2. **Customize data** in Firestore console
3. **Set up push notifications** for appointment reminders
4. **Configure analytics** for user behavior tracking
5. **Deploy to production** with proper security rules

## 🎯 Ready for Demo!

Your MedAssist app now has:
- ✅ **Real Firebase Authentication** with Google Sign-In
- ✅ **Firestore Database** for articles, doctors, and bookings
- ✅ **Cloud Messaging** for push notifications
- ✅ **Production-ready architecture**

The app will gracefully fall back to mock data if Firebase is not configured, ensuring it works in any environment!
