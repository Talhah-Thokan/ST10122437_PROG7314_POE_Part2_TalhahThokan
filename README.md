# 🏥 MedAssist - South African Healthcare App

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-orange.svg)](https://firebase.google.com)
[![License](https://img.shields.io/badge/License-Academic-yellow.svg)](LICENSE)

**MedAssist** is a comprehensive mobile healthcare application designed for South African users, providing seamless access to health articles, doctor bookings, and medical information. Built as a final POE project for BCAD PROG7314.


---

## ✨ Key Features

### 🔐 **Authentication**
- Google Sign-In via Firebase Authentication
- Guest Mode for quick access
- Persistent login state

### 📚 **Health Articles**
- 5 South African healthcare articles
- Topics: Diabetes, mental health (SADAG), medical aids, nutrition, winter health
- Full article view with author attribution
- Firebase Firestore integration

### 👨‍⚕️ **Doctor Search & Booking**
- **8 SA Doctors** across specialties (GP, Cardiology, Pediatrics, etc.)
- **Working Search** - Filter by name, specialty, or location
- **ZAR Pricing** - R650 to R1,450 per consultation
- **Real Hospitals** - Sandton, Rosebank, Milpark, Morningside, Sunninghill
- Appointment booking with date/time selection
- Booking confirmation and local storage

### ⚙️ **Settings & Preferences**
- Light/Dark theme toggle
- Notification preferences
- Language selection
- Database seeding tool
- User profile management

### 🔔 **Notifications**
- In-app booking confirmations
- Firebase Cloud Messaging ready
- Toast and Snackbar notifications

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|------------|
| **Language** | Kotlin |
| **UI Framework** | Traditional Android Views (XML + Material Components) |
| **Architecture** | MVVM-inspired |
| **Backend** | Firebase (Auth, Firestore, FCM) |
| **Database** | Firebase Firestore |
| **Authentication** | Firebase Auth (Google Sign-In) |
| **Networking** | Retrofit (ready for REST API) |
| **Build Tool** | Gradle 7.2 |
| **Min SDK** | API 21 (Android 5.0) |
| **Target SDK** | API 31 (Android 12) |

---

## 🚀 Quick Start

### **Prerequisites**
- Android Studio Arctic Fox or newer
- JDK 11
- Android SDK API 21+
- Firebase account (for full functionality)

### **1. Clone & Open**
```bash
git clone https://github.com/Talhah-Thokan/ST10122437_PROG7314_POE_Part2_TalhahThokan.git
cd ST10122437_PROG7314_POE_Part2_TalhahThokan
```

Open in Android Studio and let Gradle sync.

### **2. Firebase Setup**

#### **Option A: Use Existing Firebase Project**
The project is already configured with Firebase project `medassistpoe`.

#### **Option B: Create Your Own**
1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create a new project
3. Add Android app with package: `com.medassist.app`
4. Download `google-services.json` and place in `app/` folder
5. Enable Google Sign-In in Authentication section
6. Add your SHA-1 fingerprint:
   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```

### **3. Seed Firestore Database**
1. Run the app
2. Go to **Settings** tab
3. Tap **"Seed Firestore Database"**
4. Wait for success message
5. Restart app to see data

### **4. Build & Run**
```bash
./gradlew clean assembleDebug
./gradlew installDebug
```

Or use Android Studio's **Run** button (▶️).

---

## 📱 App Structure

### **Main Screens**
```
LoginActivity → MainActivity (Bottom Navigation)
                ├── HomeFragment
                ├── ArticlesFragment → ArticleDetailActivity
                └── BookingsFragment → BookingActivity → BookingConfirmationActivity
                    
SettingsActivity (from Home)
└── FirestoreDataSeederActivity
```

### **Navigation Tabs**
1. **Home** - Welcome message, quick actions, recent notifications
2. **Articles** - Browse health articles, tap to read full content
3. **Bookings** - Search doctors, view profiles, book appointments

---

## 🇿🇦 South African Content

### **Doctors (8 Total)**

| Doctor | Specialty | Price | Location |
|--------|-----------|-------|----------|
| Dr. Thabo Mokoena | General Practitioner | R650 | Sandton Medical Centre |
| Dr. Zanele Khumalo | Cardiologist | R1,250 | Life Healthcare, Rosebank |
| Dr. Sipho Dlamini | Pediatrician | R850 | Netcare Milpark Hospital |
| Dr. Lerato Ndlovu | Dermatologist | R950 | Morningside Mediclinic |
| Dr. Mandla Mbatha | Orthopedic Surgeon | R1,450 | Sunninghill Hospital |
| Dr. Nomvula Nkosi | Psychiatrist | R1,100 | Parktown Medical Centre |
| Dr. Bongani Zulu | Dentist | R750 | Rosebank Dental Clinic |
| Dr. Precious Mthembu | Gynecologist | R1,000 | Charlotte Maxeke Hospital |

### **Articles (5 Total)**
1. **Managing Diabetes in South Africa** - Dr. Thabo Ndlovu
2. **Winter Health Tips for South Africans** - Dr. Zanele Khumalo
3. **Mental Health Resources in South Africa** - Dr. Sipho Mkhize (SADAG)
4. **Understanding Medical Aid Schemes** - Dr. Lerato Mokoena (Discovery, Momentum)
5. **Healthy Eating on a Budget** - Dr. Nomvula Dlamini (Pap, beans, R50 meals)

---

## 🔍 Key Features Demo

### **Doctor Search**
```kotlin
// Search by specialty
"pediatrician" → Shows Dr. Sipho Dlamini

// Search by name
"Thabo" → Shows Dr. Thabo Mokoena

// Search by partial match
"cardio" → Shows Dr. Zanele Khumalo

// Search by availability
"today" → Shows all doctors available today
```

### **Booking Flow**
1. Go to **Bookings** tab
2. Search for doctor or browse list
3. Tap **"Book Appointment"**
4. Fill in patient details
5. Select date and time
6. Submit booking
7. View confirmation screen
8. Booking saved locally and in Firestore

---

## 🧪 Testing

### **Unit Tests**
```bash
./gradlew test
```

Sample tests included:
- `PreferenceManagerTest` - User preferences
- `MedAssistApiTest` - API service mocking

### **Manual Testing**
1. ✅ Google Sign-In flow
2. ✅ Guest login
3. ✅ Article browsing and detail view
4. ✅ Doctor search with various queries
5. ✅ Complete booking flow
6. ✅ Theme toggle (light/dark)
7. ✅ Database seeding
8. ✅ Offline mode (with fallback data)

---

## 🔥 Firebase Configuration

### **Firestore Collections**

#### **`articles`**
```javascript
{
  title: "Managing Diabetes in South Africa",
  author: "Dr. Thabo Ndlovu",
  summary: "...",
  content: "...",
  imageUrl: "...",
  date: "2025-10-05"
}
```

#### **`doctors`**
```javascript
{
  name: "Dr. Thabo Mokoena",
  specialty: "General Practitioner",
  rating: "4.9",
  experience: "12 years experience",
  price: "R650 per consultation",
  availability: "Available today",
  location: "Sandton Medical Centre, Johannesburg",
  searchTerms: ["general", "gp", "thabo", "mokoena", "sandton"]
}
```

#### **`bookings`**
```javascript
{
  doctorId: "abc123",
  patientName: "John Doe",
  patientEmail: "john@example.com",
  appointmentDate: "2025-10-15",
  appointmentTime: "10:00 AM",
  reason: "General checkup",
  timestamp: "2025-10-07T10:30:00Z"
}
```

### **Security Rules** (for testing)
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null; // Authenticated users only
      // For testing: allow read, write: if true;
    }
  }
}
```

---

## 🎨 Design System

### **Color Palette**
- **Primary Teal**: `#26A69A` - Healthcare trust
- **Dark Teal**: `#00796B` - Depth
- **Accent Amber**: `#FFC107` - Highlights
- **Background**: `#F5F5F5` - Clean surface
- **Text**: `#212121` - Dark grey

### **Typography**
- **Headlines**: 24sp Bold
- **Subheadings**: 18sp Bold
- **Body**: 16sp Regular
- **Captions**: 12sp Regular

---

## 📂 Project Structure

```
app/
├── src/main/
│   ├── java/com/medassist/app/
│   │   ├── data/
│   │   │   ├── api/           # Retrofit API interfaces
│   │   │   └── firebase/       # Firebase repositories & seeders
│   │   ├── services/           # FCM service
│   │   ├── ui/
│   │   │   ├── fragments/      # Home, Articles, Bookings
│   │   │   └── screens/        # Activities (Login, Settings, Booking, etc.)
│   │   └── utils/              # PreferenceManager, helpers
│   ├── res/
│   │   ├── layout/             # XML layouts
│   │   ├── values/             # Strings, colors, themes
│   │   └── drawable/           # Icons, backgrounds
│   └── AndroidManifest.xml
├── google-services.json        # Firebase config
└── build.gradle.kts            # Dependencies
```

---

## 🚧 Known Issues & Limitations

### **Current Limitations**
- Google Sign-In requires SHA-1 fingerprint configured in Firebase
- Doctor images use placeholder URLs
- Search is case-insensitive but exact substring match
- No offline-first Room database (uses Firestore only)

### **Troubleshooting**

#### **"Google Sign-In cancelled"**
- Ensure SHA-1 fingerprint is added to Firebase Console
- Check `google-services.json` has `oauth_client` entries
- Verify Google Sign-In is enabled in Firebase Authentication

#### **"No articles/doctors loading"**
- Run the database seeder: Settings → "Seed Firestore Database"
- Check Firestore rules allow read access
- Check internet connection


---

## 🤖 AI Assistance Disclaimer

This project was developed with assistance from AI technologies (Claude/ChatGPT) for:
- Code generation and boilerplate
- Architecture guidance and best practices
- Firebase integration setup
- UI/UX implementation with Material Design
- Documentation generation

**Human oversight:** All AI-generated code was reviewed, tested, modified, and validated to ensure quality, functionality, and academic integrity. The developer maintains full understanding of the codebase and architectural decisions.

---

## 📄 License

This project is created for educational purposes as part of a BCAD module. All rights reserved.

---

## 👨‍💻 Developer

**Student:** Talhah Thokan  
**Student ID:** ST10122437  
**Module:** PROG7314 - Advanced Programming  
**Institution:** IIE Varsity College Sandton
**Year:** 2025  
**Semester:** 2  

---

## 🔗 Links

- **GitHub Repository:** [ST10122437_PROG7314_POE_Part2_TalhahThokan](https://github.com/Talhah-Thokan/ST10122437_PROG7314_POE_Part2_TalhahThokan)
- **Firebase Console:** [medassistpoe](https://console.firebase.google.com)
- **Demo Video:** *(To be added)*

---

## 📞 Support

For issues or questions about this project:
1. Check the **Troubleshooting** section above
2. Review Firebase setup steps
3. Ensure all dependencies are synced
4. Try cleaning and rebuilding the project

---


*Last Updated: October 7, 2025*
