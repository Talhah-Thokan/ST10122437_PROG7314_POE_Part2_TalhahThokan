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
import com.medassist.app.R
import com.medassist.app.ui.screens.settings.SettingsActivity
import com.medassist.app.utils.PreferenceManager

/**
 * Home Fragment - Main dashboard with quick actions and recent activity
 */
class HomeFragment : Fragment() {
    
    private lateinit var profileButton: MaterialButton
    private lateinit var settingsButton: MaterialButton
    private lateinit var recentActivityRecyclerView: RecyclerView
    private lateinit var preferenceManager: PreferenceManager
    
    companion object {
        private const val TAG = "HomeFragment"
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        
        preferenceManager = PreferenceManager(requireContext())
        
        setupViews(view)
        setupClickListeners()
        setupRecentActivity()
        
        Log.d(TAG, "HomeFragment created")
        
        return view
    }
    
    private fun setupViews(view: View) {
        profileButton = view.findViewById(R.id.profileButton)
        settingsButton = view.findViewById(R.id.settingsButton)
        recentActivityRecyclerView = view.findViewById(R.id.recentActivityRecyclerView)
        
        // Set welcome message
        val userName = preferenceManager.getUserName()
        view.findViewById<android.widget.TextView>(R.id.welcomeText).text = "Welcome back, $userName!"
    }
    
    private fun setupClickListeners() {
        profileButton.setOnClickListener {
            Toast.makeText(context, "Profile feature coming soon!", Toast.LENGTH_SHORT).show()
        }
        
        settingsButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun setupRecentActivity() {
        val recentActivities = getSampleRecentActivity()
        val adapter = RecentActivityAdapter(recentActivities)
        
        recentActivityRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        recentActivityRecyclerView.adapter = adapter
    }
    
    private fun getSampleRecentActivity(): List<RecentActivity> {
        return listOf(
            RecentActivity("Article Read", "Understanding Heart Health", "2 hours ago"),
            RecentActivity("Appointment Booked", "Dr. Sarah Johnson", "Yesterday"),
            RecentActivity("Profile Updated", "Personal Information", "3 days ago")
        )
    }
}

/**
 * Data class for recent activity items
 */
data class RecentActivity(
    val type: String,
    val title: String,
    val time: String
)

/**
 * Adapter for recent activity RecyclerView
 */
class RecentActivityAdapter(private val activities: List<RecentActivity>) : 
    RecyclerView.Adapter<RecentActivityAdapter.ViewHolder>() {
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeText: android.widget.TextView = view.findViewById(R.id.activityType)
        val titleText: android.widget.TextView = view.findViewById(R.id.activityTitle)
        val timeText: android.widget.TextView = view.findViewById(R.id.activityTime)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_activity, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activities[position]
        holder.typeText.text = activity.type
        holder.titleText.text = activity.title
        holder.timeText.text = activity.time
    }
    
    override fun getItemCount(): Int = activities.size
}

