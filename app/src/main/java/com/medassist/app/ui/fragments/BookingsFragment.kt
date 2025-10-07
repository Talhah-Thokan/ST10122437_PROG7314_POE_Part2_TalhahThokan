package com.medassist.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.medassist.app.R
import com.medassist.app.data.firebase.FirebaseRepository
import com.medassist.app.ui.screens.booking.BookingActivity
import com.medassist.app.ui.screens.booking.BookingConfirmationActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Bookings Fragment - Doctor search and appointment booking
 */
class BookingsFragment : Fragment() {
    
    private lateinit var searchEditText: TextInputEditText
    private lateinit var searchButton: MaterialButton
    private lateinit var doctorsRecyclerView: RecyclerView
    private lateinit var bookingsRecyclerView: RecyclerView
    private lateinit var doctorsAdapter: DoctorsAdapter
    private lateinit var bookingsAdapter: BookingsAdapter
    
    companion object {
        private const val TAG = "BookingsFragment"
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookings, container, false)
        
        setupViews(view)
        setupClickListeners()
        setupRecyclerViews()
        
        Log.d(TAG, "BookingsFragment created")
        
        return view
    }
    
    private fun setupViews(view: View) {
        searchEditText = view.findViewById(R.id.searchEditText)
        searchButton = view.findViewById(R.id.searchButton)
        doctorsRecyclerView = view.findViewById(R.id.doctorsRecyclerView)
        bookingsRecyclerView = view.findViewById(R.id.bookingsRecyclerView)
    }
    
    private fun setupClickListeners() {
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotBlank()) {
                searchDoctors(query)
            } else {
                Toast.makeText(context, "Please enter a search term", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupRecyclerViews() {
        // Setup doctors list
        val doctors = getSampleDoctors()
        doctorsAdapter = DoctorsAdapter(doctors) { doctor ->
            // Navigate to booking screen
            val intent = Intent(requireContext(), BookingActivity::class.java)
            intent.putExtra("DOCTOR_NAME", doctor.name)
            intent.putExtra("DOCTOR_SPECIALTY", doctor.specialty)
            intent.putExtra("DOCTOR_ID", doctor.id)
            startActivity(intent)
        }
        
        doctorsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        doctorsRecyclerView.adapter = doctorsAdapter
        
        // Setup bookings list
        val bookings = getSampleBookings()
        bookingsAdapter = BookingsAdapter(bookings) { booking ->
            // Show booking details
            Toast.makeText(context, "Booking: ${booking.doctorName} on ${booking.date}", Toast.LENGTH_SHORT).show()
        }
        
        bookingsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        bookingsRecyclerView.adapter = bookingsAdapter
    }
    
    private fun searchDoctors(query: String) {
        Toast.makeText(context, "Searching for: $query", Toast.LENGTH_SHORT).show()
        // TODO: Implement actual API call to /providers endpoint
        // For now, we'll just show a message
    }
    
    private fun getSampleDoctors(): List<Doctor> {
        return listOf(
            Doctor(
                "1",
                "Dr. Sarah Johnson",
                "Cardiologist",
                "4.8",
                "2.1 km away",
                "15+ years experience",
                "$150 per visit",
                "Available today"
            ),
            Doctor(
                "2",
                "Dr. Michael Chen",
                "General Practitioner",
                "4.9",
                "0.8 km away",
                "12+ years experience",
                "$120 per visit",
                "Available tomorrow"
            ),
            Doctor(
                "3",
                "Dr. Emily Rodriguez",
                "Dermatologist",
                "4.7",
                "3.2 km away",
                "8+ years experience",
                "$180 per visit",
                "Available next week"
            ),
            Doctor(
                "4",
                "Dr. James Wilson",
                "Orthopedist",
                "4.6",
                "1.5 km away",
                "20+ years experience",
                "$200 per visit",
                "Available today"
            ),
            Doctor(
                "5",
                "Dr. Lisa Thompson",
                "Pediatrician",
                "4.9",
                "2.8 km away",
                "10+ years experience",
                "$130 per visit",
                "Available tomorrow"
            )
        )
    }
    
    private fun getSampleBookings(): List<Booking> {
        return listOf(
            Booking(
                "1",
                "Dr. Sarah Johnson",
                "Cardiologist",
                "2025-01-20",
                "10:00 AM",
                "Confirmed"
            ),
            Booking(
                "2",
                "Dr. Michael Chen",
                "General Practitioner",
                "2025-01-22",
                "2:30 PM",
                "Pending"
            )
        )
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh bookings when returning from booking screen
        // This simulates getting updated booking data
    }
}

/**
 * Data classes for doctors and bookings
 */
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

data class Booking(
    val id: String,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val status: String
)

/**
 * Adapter for doctors RecyclerView
 */
class DoctorsAdapter(
    private val doctors: List<Doctor>,
    private val onItemClick: (Doctor) -> Unit
) : RecyclerView.Adapter<DoctorsAdapter.ViewHolder>() {
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: android.widget.TextView = view.findViewById(R.id.doctorName)
        val specialtyText: android.widget.TextView = view.findViewById(R.id.doctorSpecialty)
        val ratingText: android.widget.TextView = view.findViewById(R.id.doctorRating)
        val distanceText: android.widget.TextView = view.findViewById(R.id.doctorDistance)
        val priceText: android.widget.TextView = view.findViewById(R.id.doctorPrice)
        val availabilityText: android.widget.TextView = view.findViewById(R.id.doctorAvailability)
        val bookButton: MaterialButton = view.findViewById(R.id.bookButton)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor_modern, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doctor = doctors[position]
        
        holder.nameText.text = doctor.name
        holder.specialtyText.text = doctor.specialty
        holder.ratingText.text = "⭐ ${doctor.rating}"
        holder.distanceText.text = doctor.distance
        holder.priceText.text = doctor.price
        holder.availabilityText.text = doctor.availability
        
        holder.bookButton.setOnClickListener {
            onItemClick(doctor)
        }
    }
    
    override fun getItemCount(): Int = doctors.size
}

/**
 * Adapter for bookings RecyclerView
 */
class BookingsAdapter(
    private val bookings: List<Booking>,
    private val onItemClick: (Booking) -> Unit
) : RecyclerView.Adapter<BookingsAdapter.ViewHolder>() {
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val doctorNameText: android.widget.TextView = view.findViewById(R.id.bookingDoctorName)
        val specialtyText: android.widget.TextView = view.findViewById(R.id.bookingSpecialty)
        val dateText: android.widget.TextView = view.findViewById(R.id.bookingDate)
        val timeText: android.widget.TextView = view.findViewById(R.id.bookingTime)
        val statusText: android.widget.TextView = view.findViewById(R.id.bookingStatus)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val booking = bookings[position]
        
        holder.doctorNameText.text = booking.doctorName
        holder.specialtyText.text = booking.specialty
        holder.dateText.text = booking.date
        holder.timeText.text = booking.time
        holder.statusText.text = booking.status
        
        // Set status color
        val statusColor = when (booking.status) {
            "Confirmed" -> android.graphics.Color.parseColor("#4CAF50")
            "Pending" -> android.graphics.Color.parseColor("#FF9800")
            else -> android.graphics.Color.parseColor("#757575")
        }
        holder.statusText.setTextColor(statusColor)
        
        holder.itemView.setOnClickListener {
            onItemClick(booking)
        }
    }
    
    override fun getItemCount(): Int = bookings.size
}

