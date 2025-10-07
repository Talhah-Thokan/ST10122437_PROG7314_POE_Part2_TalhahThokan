# 🔐 Enable Google Sign-In - Quick Guide

## ✅ Your SHA-1 Certificate:
```
DE:56:60:91:FF:09:C9:98:5D:3F:E0:6C:3A:D1:67:32:AE:0F:C1:28
```

---

## 🚀 Steps to Enable Google Sign-In (5 minutes):

### **1. Add SHA-1 to Firebase**

**Link:** https://console.firebase.google.com/project/medassistpoe/settings/general

**Steps:**
1. Scroll to **"Your apps"**
2. Find Android app (`com.medassist.app`)
3. Click **"Add fingerprint"**
4. Paste: `DE:56:60:91:FF:09:C9:98:5D:3F:E0:6C:3A:D1:67:32:AE:0F:C1:28`
5. Click **"Save"**

---

### **2. Download NEW google-services.json**

**Important:** After adding SHA-1, you MUST download a new file!

1. Same page (Project Settings)
2. Scroll to your Android app
3. Click **"google-services.json"** download icon
4. **Replace** file in `app/` folder

---

### **3. Enable Google Authentication**

**Link:** https://console.firebase.google.com/project/medassistpoe/authentication

**Steps:**
1. Click **Authentication** in left menu
2. If not enabled, click **"Get started"**
3. Go to **"Sign-in method"** tab
4. Click **"Google"** provider
5. Toggle **"Enable"** to ON
6. **Support email:** Enter your email
7. Click **"Save"**

---

### **4. Rebuild & Test**

```bash
# Clean
./gradlew clean

# Build
./gradlew assembleDebug

# Uninstall old version
adb uninstall com.medassist.app

# Install new version
./gradlew installDebug
```

---

### **5. Test Google Sign-In**

1. Open app
2. Tap **"Sign in with Google"**
3. Select your Google account
4. Should see: "Welcome, [Your Name]!"
5. Check Firebase Console → Authentication → Users (you should appear!)

---

## 🔍 Expected Result:

### **Before (Current):**
```
W/MedAssistLoginActivity: User cancelled Google Sign-In
```

### **After (Success):**
```
D/FirebaseAuthManager: Google Sign-In configured successfully
D/FirebaseRepository: User profile saved successfully: [your-uid]
I/MedAssistLoginActivity: Welcome, [Your Name]!
```

---

## 🆘 Troubleshooting:

### **If still cancelled/fails:**

1. **Check SHA-1 is correct:**
   ```bash
   ./gradlew signingReport | grep SHA1
   ```

2. **Verify it's added in Firebase:**
   - Project Settings → Your apps → Should see SHA-1 listed

3. **Make sure you downloaded NEW google-services.json:**
   - File must be downloaded AFTER adding SHA-1

4. **Check Google Sign-In is enabled:**
   - Authentication → Sign-in method → Google should be ON

5. **Clean rebuild:**
   ```bash
   ./gradlew clean
   rm -rf app/build
   ./gradlew assembleDebug
   ```

---

## ✅ Success Checklist:

- [ ] SHA-1 added to Firebase Console
- [ ] Downloaded NEW google-services.json
- [ ] Replaced old file in app/ folder
- [ ] Google Sign-In enabled in Authentication
- [ ] Support email added
- [ ] App cleaned and rebuilt
- [ ] Old app uninstalled
- [ ] New app installed
- [ ] Tested sign-in

---

## 🎯 Your Project Details:

- **Project Name:** MedAssistPOE
- **Project ID:** medassistpoe
- **Project Number:** 564965961341
- **Package Name:** com.medassist.app
- **SHA-1:** `DE:56:60:91:FF:09:C9:98:5D:3F:E0:6C:3A:D1:67:32:AE:0F:C1:28`

---

**After completing these steps, Google Sign-In will work!** 🎉

