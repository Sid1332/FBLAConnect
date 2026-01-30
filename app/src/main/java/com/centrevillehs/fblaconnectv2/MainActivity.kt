package com.centrevillehs.fblaconnectv2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var firstName = ""
    private var lastName = ""
    private var memberID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if logged in
        val prefs = getSharedPreferences("FBLAPrefs", MODE_PRIVATE)
        firstName = prefs.getString("firstName", "") ?: ""
        lastName = prefs.getString("lastName", "") ?: ""
        memberID = prefs.getString("memberID", "") ?: ""

        if (firstName.isEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val contentText = findViewById<TextView>(R.id.contentText)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        val socialMediaButtons = findViewById<LinearLayout>(R.id.socialMediaButtons)

        // Calendar views
        val calendarContainer = findViewById<LinearLayout>(R.id.calendarContainer)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val eventDetailsText = findViewById<TextView>(R.id.eventDetailsText)
        val contentArea = findViewById<ScrollView>(R.id.contentArea)

        val btnInstagram = findViewById<Button>(R.id.btnInstagram)
        val btnTwitter = findViewById<Button>(R.id.btnTwitter)
        val btnFacebook = findViewById<Button>(R.id.btnFacebook)

        // Social media button clicks
        btnInstagram.setOnClickListener {
            openUrl("https://www.instagram.com/fbla_national/")
        }
        btnTwitter.setOnClickListener {
            openUrl("https://x.com/FBLA_National")
        }
        btnFacebook.setOnClickListener {
            openUrl("https://www.facebook.com/FutureBusinessLeaders/")
        }

        // Create menu items
        bottomNav.menu.add(0, 1, 0, "Profile").setIcon(android.R.drawable.ic_menu_myplaces)
        bottomNav.menu.add(0, 2, 1, "Events").setIcon(android.R.drawable.ic_menu_today)
        bottomNav.menu.add(0, 3, 2, "Resources").setIcon(android.R.drawable.ic_menu_info_details)
        bottomNav.menu.add(0, 4, 3, "News").setIcon(android.R.drawable.ic_menu_report_image)

        // Show Profile by default
        showProfile(contentText, socialMediaButtons, contentArea, calendarContainer)

        // Handle bottom nav clicks
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                1 -> showProfile(contentText, socialMediaButtons, contentArea, calendarContainer)
                2 -> showEventsCalendar(calendarView, eventDetailsText, contentArea, calendarContainer)
                3 -> showResources(contentText, socialMediaButtons, contentArea, calendarContainer)
                4 -> showNews(contentText, socialMediaButtons, contentArea, calendarContainer)
            }
            true
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Opening: $url", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEventsCalendar(
        calendarView: CalendarView,
        eventText: TextView,
        contentArea: ScrollView,
        calendarContainer: LinearLayout
    ) {
        // Hide text content, show calendar
        contentArea.visibility = View.GONE
        calendarContainer.visibility = View.VISIBLE

        // Sample events data - in a real app, this would come from your database
        val events = mapOf(
            "2/15/2026" to """
                📅 February 15, 2026
                
                📍 Regional Conference
                🕒 9:00 AM - 5:00 PM
                📍 Convention Center
                
                👥 125 members registered
                ✅ You're registered!
                
                📋 Agenda:
                • Opening Keynote
                • Workshops & Sessions
                • Networking Lunch
                • Awards Ceremony
            """.trimIndent(),

            "3/22/2026" to """
                📅 March 22, 2026
                
                📍 State Competition
                🕒 8:00 AM - 6:00 PM
                📍 State University
                
                👥 200 competitors registered
                ⏰ Registration closes March 15
                
                🏆 Competitions:
                • Mobile App Development
                • Business Presentations
                • Marketing
                • Accounting
            """.trimIndent(),

            "6/28/2026" to """
                📅 June 28 - July 1, 2026
                
                📍 National Leadership Conference
                🕒 Multi-day Event
                📍 Atlanta, GA
                
                🌟 The biggest FBLA event of the year!
                
                ✈️ Travel Info:
                • Hotel reservations required
                • Group flight available
                • Transportation provided
                
                🎯 Activities:
                • National Competitions
                • Workshops & Training
                • Career Fair
                • Networking Events
                • Awards Gala
            """.trimIndent()
        )

        // Set default message
        eventText.text = "👆 Select a date to view events\n\n📅 Upcoming Events:\n• Feb 15 - Regional Conference\n• Mar 22 - State Competition\n• Jun 28-Jul 1 - National Leadership"

        // Handle date selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val dateKey = "${month + 1}/$dayOfMonth/$year"

            eventText.text = if (events.containsKey(dateKey)) {
                events[dateKey]
            } else {
                """
                    📅 ${month + 1}/$dayOfMonth/$year
                    
                    No events scheduled for this date.
                    
                    📅 Upcoming Events:
                    • Feb 15 - Regional Conference
                    • Mar 22 - State Competition  
                    • Jun 28-Jul 1 - National Leadership
                    
                    💡 Tap on an event date above to see details!
                """.trimIndent()
            }
        }
    }

    private fun showProfile(
        textView: TextView,
        socialButtons: LinearLayout,
        contentArea: ScrollView,
        calendarContainer: LinearLayout
    ) {
        // Show text content, hide calendar
        contentArea.visibility = View.VISIBLE
        calendarContainer.visibility = View.GONE
        socialButtons.visibility = View.VISIBLE

        textView.text = """
            📱 MEMBER PROFILE
            
            Name: $firstName $lastName
            Chapter: Central High School
            Member ID: $memberID
            
            ✓ Event Calendar Access
            ✓ Competition Reminders
            ✓ Resource Library
            ✓ Social Media Connected
            
            Member Since: 2024
            Events Attended: 5
            
            🔗 Connect with FBLA on social media:
        """.trimIndent()
    }

    private fun showResources(
        textView: TextView,
        socialButtons: LinearLayout,
        contentArea: ScrollView,
        calendarContainer: LinearLayout
    ) {
        // Show text content, hide calendar
        contentArea.visibility = View.VISIBLE
        calendarContainer.visibility = View.GONE
        socialButtons.visibility = View.GONE

        textView.text = """
            📚 FBLA RESOURCES
            
            Competition Materials:
            • Mobile App Development Guidelines
            • Competition Topics 2025-2026
            • Rating Sheets & Rubrics
            
            Member Documents:
            • Honor Code & Code of Conduct
            • Dress Code Requirements
            • Chapter Handbook
            • Business Skills Resources
            
            Quick Links:
            • FBLA National Website
            • State Chapter Portal
            • code.org Partnership Resources
            
            🔗 Tap any resource to open
        """.trimIndent()
    }

    private fun showNews(
        textView: TextView,
        socialButtons: LinearLayout,
        contentArea: ScrollView,
        calendarContainer: LinearLayout
    ) {
        // Show text content, hide calendar
        contentArea.visibility = View.VISIBLE
        calendarContainer.visibility = View.GONE
        socialButtons.visibility = View.VISIBLE

        textView.text = """
            📰 NEWS & UPDATES
            
            🔥 NEW: 2025-2026 Topics Released!
            Mobile App Development competition
            focuses on member engagement apps.
            
            📱 Recent Announcements:
            • Registration Now Open for NLC
            • New code.org Partnership Launched
            • Chapter Social Media Integration
            • Member Success Stories Featured
            
            🏆 Chapter Highlights:
            • 15 members qualified for States
            • Community service project completed
            • New officers elected
            
            📱 Follow us for updates:
        """.trimIndent()
    }
}