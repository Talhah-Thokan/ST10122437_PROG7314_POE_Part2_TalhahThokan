# ⚡ Quick Firebase Setup - 5 Minute Guide

## 🎯 Essential Steps Only

### 1️⃣ Get SHA-1 Certificate (2 min)

```bash
cd /Users/talhahthokan/Desktop/BCAD/2025/Semester\ 2/PROG7314/PROG7314/2025/POE/ST10122437_PROG7314_POE_Part2_TalhahThokan
./gradlew signingReport
```

**Copy the SHA1 value** (looks like: `A1:B2:C3:D4:...`)

---

### 2️⃣ Firebase Console Setup (3 min)

1. **Go to**: [console.firebase.google.com](https://console.firebase.google.com)

2. **Create Project** → Name: `MedAssist`

3. **Add Android App**:
   - Package: `com.medassist.app`
   - Download `google-services.json` → Save to `app/` folder

4. **Project Settings** → Add your SHA-1 fingerprint → **Save**

5. **Authentication** → Sign-in method → **Enable Google** → Save

6. **Firestore Database** → Create → **Test mode** → Enable

---

### 3️⃣ Add Sample Data to Firestore (2 min)

#### Collection: `articles`

**Document 1:**
- title: `"5 Essential Tips for Better Sleep"`
- author: `"Dr. Sarah Johnson"`
- summary: `"Learn essential techniques to improve sleep."`
- content: `"Sleep is crucial for health..."`
- imageUrl: `"https://via.placeholder.com/300x200"`
- date: `"2025-01-15"`

**Add 2-3 more articles**

#### Collection: `doctors`

**Document 1:**
- name: `"Dr. Sarah Johnson"`
- specialty: `"Cardiologist"`
- rating: `"4.8"`
- distance: `"2.1 km away"`
- experience: `"15+ years"`
- price: `"$150 per visit"`
- availability: `"Available today"`
- searchTerms: `["cardiology", "heart", "sarah"]`

**Add 2-3 more doctors**

---

### 4️⃣ Update Firestore Rules (1 min)

**Firestore Database → Rules:**

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

Click **Publish**

---

### 5️⃣ Rebuild App (1 min)

```bash
./gradlew clean
./gradlew assembleDebug
```

---

## ✅ Test It!

1. **Open app**
2. **Tap "Sign in with Google"**
3. **Select Google account**
4. ✅ **Should sign in successfully!**
5. ✅ **Articles load from Firestore**
6. ✅ **No more "Failed to retrieve Firebase Instance Id"**

---

## 🔍 Quick Check

### File Location:
```bash
ls app/google-services.json
```
Should show the file exists.

### Console Logs (Success):
```
D/FirebaseAuthManager: Google Sign-In configured successfully
D/FirebaseRepository: Fetched X articles from Firestore
```

---

## 🆘 Still Having Issues?

### Common Fix:
```bash
# 1. Clean everything
./gradlew clean

# 2. Delete build folders
rm -rf app/build
rm -rf build

# 3. Rebuild
./gradlew assembleDebug
```

### Check:
- [ ] `google-services.json` in `app/` folder
- [ ] SHA-1 added to Firebase Console
- [ ] Google Sign-In **enabled** in Firebase
- [ ] Firestore **created**
- [ ] Sample data added to Firestore

---

## 🎯 That's It!

**Total Time: ~10 minutes**

Your app now has:
- ✅ Real Google Sign-In
- ✅ Live Firestore database
- ✅ Cloud data sync
- ✅ Production-ready Firebase

See `COMPLETE_FIREBASE_SETUP.md` for detailed troubleshooting.

