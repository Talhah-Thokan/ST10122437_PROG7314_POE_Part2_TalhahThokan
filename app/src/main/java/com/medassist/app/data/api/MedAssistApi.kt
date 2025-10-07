package com.medassist.app.data.api

import retrofit2.Response
import retrofit2.http.*

/**
 * API interface for MedAssist backend services
 */
interface MedAssistApi {

    @GET("articles")
    suspend fun getArticles(): Response<List<Article>>

    @GET("providers")
    suspend fun getProviders(): Response<List<Doctor>>

    @POST("bookings")
    suspend fun createBooking(@Body booking: BookingRequest): Response<BookingResponse>
}

/**
 * Data classes for API requests and responses
 */
data class Article(
    val id: String,
    val title: String,
    val author: String,
    val summary: String,
    val content: String,
    val imageUrl: String,
    val date: String
)

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val rating: String,
    val distance: String,
    val experience: String,
    val price: String,
    val availability: String
)

data class BookingRequest(
    val doctorId: String? = null,
    val patientName: String,
    val patientEmail: String,
    val appointmentDate: String,
    val appointmentTime: String,
    val reason: String
)

data class BookingResponse(
    val id: String,
    val status: String,
    val message: String
)