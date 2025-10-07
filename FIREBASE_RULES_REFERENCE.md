# 🔥 Firebase Firestore Rules - Complete Reference

## 📋 Current Rules (Development Mode)

### For Testing & Demo - Open Access

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow all reads and writes (for development only)
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

**Use this for**: Development, testing, demonstrations
**Security**: ⚠️ **Low** - Anyone can read/write all data

---

## 🔒 Production Rules - Secure Access

### Recommended for Production Deployment

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // ARTICLES COLLECTION
    // Anyone can read, only authenticated users can write
    match /articles/{articleId} {
      allow read: if true;  // Public reading
      allow create, update, delete: if request.auth != null;  // Must be logged in to write
    }
    
    // DOCTORS COLLECTION
    // Anyone can read, only authenticated users can write
    match /doctors/{doctorId} {
      allow read: if true;  // Public reading
      allow create, update, delete: if request.auth != null;  // Must be logged in to write
    }
    
    // USERS COLLECTION
    // Users can only read/write their own profile
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // BOOKINGS COLLECTION
    // Users can create bookings and read their own bookings
    match /bookings/{bookingId} {
      allow create: if request.auth != null;  // Anyone logged in can create
      allow read: if request.auth != null && 
                     resource.data.userId == request.auth.uid;  // Only read your own
      allow update, delete: if request.auth != null && 
                               resource.data.userId == request.auth.uid;  // Only modify your own
    }
  }
}
```

**Use this for**: Production apps, real deployments
**Security**: ✅ **High** - Proper access control

---

## 🎓 Advanced Rules - Admin Support

### With Admin Role Support

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // Helper function to check if user is admin
    function isAdmin() {
      return request.auth != null && 
             request.auth.token.admin == true;
    }
    
    // Helper function to check if user is authenticated
    function isSignedIn() {
      return request.auth != null;
    }
    
    // Helper function to check if user owns the resource
    function isOwner(userId) {
      return request.auth != null && 
             request.auth.uid == userId;
    }
    
    // ARTICLES COLLECTION
    match /articles/{articleId} {
      allow read: if true;  // Public reading
      allow create, update, delete: if isAdmin();  // Only admins can manage articles
    }
    
    // DOCTORS COLLECTION
    match /doctors/{doctorId} {
      allow read: if true;  // Public reading
      allow create, update, delete: if isAdmin();  // Only admins can manage doctors
    }
    
    // USERS COLLECTION
    match /users/{userId} {
      allow read: if isSignedIn();  // Any logged-in user can read user profiles
      allow create, update: if isOwner(userId);  // Users can create/update their own profile
      allow delete: if isAdmin();  // Only admins can delete users
    }
    
    // BOOKINGS COLLECTION
    match /bookings/{bookingId} {
      allow create: if isSignedIn();  // Any logged-in user can book
      allow read: if isOwner(resource.data.userId) || isAdmin();  // Users see their own, admins see all
      allow update: if isOwner(resource.data.userId) || isAdmin();  // Users update their own, admins update any
      allow delete: if isAdmin();  // Only admins can delete bookings
    }
    
    // ADMIN COLLECTION (example - for admin-only data)
    match /admin/{document=**} {
      allow read, write: if isAdmin();
    }
  }
}
```

**Use this for**: Enterprise apps, multi-user systems
**Security**: ✅✅ **Very High** - Role-based access control

---

## 🔑 How to Set Custom Claims (Admin Role)

To make a user an admin, use Firebase Admin SDK or Cloud Functions:

### Using Firebase Admin SDK (Node.js)

```javascript
const admin = require('firebase-admin');

// Set admin custom claim on user
admin.auth().setCustomUserClaims(uid, {admin: true})
  .then(() => {
    console.log('Admin privileges granted to user:', uid);
  });
```

### Using Firebase Console (Manual)

1. Go to Firebase Console
2. Authentication → Users
3. Click on user
4. Copy their UID
5. Use Firebase CLI or Admin SDK to set custom claim

---

## 📊 Rule Variables Reference

### Available Variables:

| Variable | Description | Example |
|----------|-------------|---------|
| `request.auth` | Authentication info | `request.auth != null` |
| `request.auth.uid` | User ID | `request.auth.uid == userId` |
| `request.auth.token` | Token with custom claims | `request.auth.token.admin` |
| `resource.data` | Existing document data | `resource.data.userId` |
| `request.resource.data` | Incoming document data | `request.resource.data.title` |
| `request.time` | Current time | `request.time < timestamp` |

---

## 🧪 Testing Rules

### Test in Firebase Console

1. Go to **Firestore Database**
2. Click **Rules** tab
3. Click **Rules Playground**
4. Select operation (read/write)
5. Enter document path
6. Simulate authenticated user
7. Click **Run**

### Example Tests:

**Test 1: Unauthenticated user reading articles**
- Location: `/articles/article1`
- Auth: Not signed in
- Operation: get
- ✅ Should **allow**

**Test 2: User reading another user's booking**
- Location: `/bookings/booking1`
- Auth: Signed in (different userId)
- Operation: get
- ❌ Should **deny**

**Test 3: User creating a booking**
- Location: `/bookings/newBooking`
- Auth: Signed in
- Operation: create
- ✅ Should **allow**

---

## 🎯 Choosing the Right Rules

### For Your POE/Demo:

```javascript
// Simple & Works Everywhere
match /{document=**} {
  allow read, write: if true;
}
```

✅ **Pros**: Works immediately, no auth issues
❌ **Cons**: Not secure for real data

### For Real Deployment:

```javascript
// Secure & Professional
match /articles/{articleId} {
  allow read: if true;
  allow write: if request.auth != null;
}
```

✅ **Pros**: Secure, professional, production-ready
⚠️ **Note**: Requires users to be authenticated

---

## 🚀 Recommended Approach

### During Development:
Use **Development Mode** rules (open access) for easy testing

### Before Demo:
Switch to **Production Rules** to show security knowledge

### For Marking:
Use **Development Mode** so markers can test without auth issues

### For Real App:
Use **Advanced Rules** with admin support

---

## 📝 How to Update Rules

1. Go to Firebase Console
2. Navigate to **Firestore Database**
3. Click **Rules** tab
4. Copy and paste the rules you want
5. Click **Publish**
6. Rules take effect immediately

---

## ⚠️ Important Notes

1. **Test mode expires after 30 days** - Update rules before expiry
2. **Rules apply to all reads/writes** - No exceptions
3. **Rules don't filter data** - They only allow/deny access
4. **Invalid rules = database lockout** - Test in Rules Playground first
5. **Rules are evaluated top to bottom** - More specific rules first

---

## 🎓 Best Practices

✅ **DO:**
- Start with restrictive rules
- Use helper functions for readability
- Test rules in the playground
- Document your rules with comments
- Use different rules for dev/prod

❌ **DON'T:**
- Use `allow read, write: if true` in production
- Trust client-side validation only
- Share admin credentials
- Forget to test rules
- Deploy without backup rules

---

## 📚 Learn More

- [Firebase Security Rules Docs](https://firebase.google.com/docs/firestore/security/get-started)
- [Rules Language Reference](https://firebase.google.com/docs/rules/rules-language)
- [Common Rules Patterns](https://firebase.google.com/docs/firestore/security/rules-conditions)

---

## ✅ Quick Reference

**Allow anyone to read:**
```javascript
allow read: if true;
```

**Require authentication:**
```javascript
allow write: if request.auth != null;
```

**User owns resource:**
```javascript
allow write: if request.auth.uid == resource.data.userId;
```

**Admin only:**
```javascript
allow write: if request.auth.token.admin == true;
```

**Validate data:**
```javascript
allow create: if request.resource.data.title is string;
```

---

**Your app is already configured with the Development Mode rules for easy testing!**

