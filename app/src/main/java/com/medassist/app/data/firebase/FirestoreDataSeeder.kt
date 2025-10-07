package com.medassist.app.data.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Firestore Data Seeder
 * Populates the Firestore database with sample data for development and testing
 */
class FirestoreDataSeeder {
    
    private val firestore = FirebaseFirestore.getInstance()
    
    companion object {
        private const val TAG = "FirestoreDataSeeder"
        
        // Collection names
        private const val COLLECTION_ARTICLES = "articles"
        private const val COLLECTION_DOCTORS = "doctors"
    }
    
    /**
     * Seeds the database with sample articles
     */
    suspend fun seedArticles() {
        try {
            val articles = listOf(
                mapOf(
                    "title" to "5 Essential Tips for Better Sleep",
                    "author" to "Dr. Sarah Johnson",
                    "summary" to "Learn essential techniques to improve your sleep quality and overall health.",
                    "content" to "Sleep is crucial for overall health and well-being. Learn these proven techniques to improve your sleep quality and overall health. This comprehensive guide covers everything from sleep hygiene to relaxation techniques that can help you get the rest you need.",
                    "imageUrl" to "https://via.placeholder.com/300x200/008B8B/FFFFFF?text=Sleep+Tips",
                    "date" to "2025-01-15"
                ),
                mapOf(
                    "title" to "Understanding Heart Health",
                    "author" to "Dr. Michael Chen",
                    "summary" to "Your heart is your most important muscle. Discover the key factors that contribute to cardiovascular health.",
                    "content" to "Everything you need to know about maintaining a healthy heart and preventing cardiovascular diseases. This article covers diet, exercise, and lifestyle changes that can significantly improve your heart health.",
                    "imageUrl" to "https://via.placeholder.com/300x200/20B2AA/FFFFFF?text=Heart+Health",
                    "date" to "2025-01-14"
                ),
                mapOf(
                    "title" to "Mental Health and Wellness",
                    "author" to "Dr. Emily Rodriguez",
                    "summary" to "Mental health is just as important as physical health. Explore practical strategies for managing stress.",
                    "content" to "Practical strategies for managing stress and maintaining good mental health. Learn about mindfulness, meditation, and other techniques that can help you maintain emotional well-being.",
                    "imageUrl" to "https://via.placeholder.com/300x200/006666/FFFFFF?text=Mental+Health",
                    "date" to "2025-01-13"
                ),
                mapOf(
                    "title" to "Nutrition for Active Living",
                    "author" to "Dr. James Wilson",
                    "summary" to "Fuel your body with the right nutrients to support an active lifestyle. Discover the best foods.",
                    "content" to "Discover the best foods to fuel your active lifestyle and maintain optimal health. This guide covers macronutrients, micronutrients, and meal planning strategies.",
                    "imageUrl" to "https://via.placeholder.com/300x200/008B8B/FFFFFF?text=Nutrition",
                    "date" to "2025-01-12"
                ),
                mapOf(
                    "title" to "Preventing Common Cold",
                    "author" to "Dr. Lisa Thompson",
                    "summary" to "Boost your immunity and avoid seasonal illnesses with these simple but effective prevention strategies.",
                    "content" to "Simple steps to boost your immunity and avoid seasonal illnesses. Learn about vitamins, hygiene practices, and lifestyle changes that can help you stay healthy year-round.",
                    "imageUrl" to "https://via.placeholder.com/300x200/20B2AA/FFFFFF?text=Cold+Prevention",
                    "date" to "2025-01-11"
                )
            )
            
            for (article in articles) {
                firestore.collection(COLLECTION_ARTICLES)
                    .add(article)
                    .await()
            }
            
            Log.d(TAG, "Successfully seeded ${articles.size} articles")
        } catch (e: Exception) {
            Log.e(TAG, "Error seeding articles", e)
        }
    }
    
    /**
     * Seeds the database with sample doctors
     */
    suspend fun seedDoctors() {
        try {
            val doctors = listOf(
                mapOf(
                    "name" to "Dr. Sarah Johnson",
                    "specialty" to "Cardiologist",
                    "rating" to "4.8",
                    "distance" to "2.1 km away",
                    "experience" to "15+ years experience",
                    "price" to "$150 per visit",
                    "availability" to "Available today",
                    "searchTerms" to listOf("cardiology", "heart", "cardiovascular", "sarah", "johnson")
                ),
                mapOf(
                    "name" to "Dr. Michael Chen",
                    "specialty" to "General Practitioner",
                    "rating" to "4.9",
                    "distance" to "0.8 km away",
                    "experience" to "12+ years experience",
                    "price" to "$120 per visit",
                    "availability" to "Available tomorrow",
                    "searchTerms" to listOf("general", "family", "doctor", "michael", "chen")
                ),
                mapOf(
                    "name" to "Dr. Emily Rodriguez",
                    "specialty" to "Dermatologist",
                    "rating" to "4.7",
                    "distance" to "3.2 km away",
                    "experience" to "8+ years experience",
                    "price" to "$180 per visit",
                    "availability" to "Available next week",
                    "searchTerms" to listOf("dermatology", "skin", "dermatologist", "emily", "rodriguez")
                ),
                mapOf(
                    "name" to "Dr. James Wilson",
                    "specialty" to "Orthopedist",
                    "rating" to "4.6",
                    "distance" to "1.5 km away",
                    "experience" to "20+ years experience",
                    "price" to "$200 per visit",
                    "availability" to "Available today",
                    "searchTerms" to listOf("orthopedics", "bone", "joint", "james", "wilson")
                ),
                mapOf(
                    "name" to "Dr. Lisa Thompson",
                    "specialty" to "Pediatrician",
                    "rating" to "4.9",
                    "distance" to "2.8 km away",
                    "experience" to "10+ years experience",
                    "price" to "$130 per visit",
                    "availability" to "Available tomorrow",
                    "searchTerms" to listOf("pediatrics", "children", "kids", "lisa", "thompson")
                ),
                mapOf(
                    "name" to "Dr. David Park",
                    "specialty" to "Neurologist",
                    "rating" to "4.8",
                    "distance" to "4.1 km away",
                    "experience" to "18+ years experience",
                    "price" to "$220 per visit",
                    "availability" to "Available next week",
                    "searchTerms" to listOf("neurology", "brain", "nervous", "david", "park")
                )
            )
            
            for (doctor in doctors) {
                firestore.collection(COLLECTION_DOCTORS)
                    .add(doctor)
                    .await()
            }
            
            Log.d(TAG, "Successfully seeded ${doctors.size} doctors")
        } catch (e: Exception) {
            Log.e(TAG, "Error seeding doctors", e)
        }
    }
    
    /**
     * Seeds all sample data
     */
    suspend fun seedAllData() {
        Log.d(TAG, "Starting database seeding...")
        
        try {
            seedArticles()
            seedDoctors()
            
            Log.d(TAG, "Database seeding completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error during database seeding", e)
        }
    }
}
