package com.centrevillehs.fblaconnectv2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

//VARIABLES (CAMEL CASED) (For now val, once we can change them then we do var, but idk how to do storage so)
val firstName = "Sahir"
val lastName = "Abrar"
//END OF VARIABLES

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contentText = findViewById<TextView>(R.id.contentText)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Create menu items
        bottomNav.menu.add(0, 1, 0, "Profile").setIcon(android.R.drawable.ic_menu_myplaces)
        bottomNav.menu.add(0, 2, 1, "Events").setIcon(android.R.drawable.ic_menu_today)
        bottomNav.menu.add(0, 3, 2, "Resources").setIcon(android.R.drawable.ic_menu_info_details)
        bottomNav.menu.add(0, 4, 3, "News").setIcon(android.R.drawable.ic_menu_report_image)

        // Show Profile by default
        showProfile(contentText)

        // Handle bottom nav clicks
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                1 -> showProfile(contentText)
                2 -> showEvents(contentText)
                3 -> showResources(contentText)
                4 -> showNews(contentText)
            }
            true
        }
    }

    private fun showProfile(textView: TextView) {
        textView.text = """
            📱 MEMBER PROFILE
            
            Name: John Doe
            Chapter: Central High School
            Member ID: 12345
            
            ✓ Event Calendar Access
            ✓ Competition Reminders
            ✓ Resource Library
            ✓ Social Media Connected
            
            Member Since: 2024
            Events Attended: 5
        """.trimIndent()
    }

    private fun showEvents(textView: TextView) {
        textView.text = """
            📅 UPCOMING EVENTS
            
            • Regional Conference
              Feb 15, 2026 - 9:00 AM
              Location: Convention Center
            
            • State Competition
              Mar 22, 2026 - 8:00 AM
              Location: State University
            
            • National Leadership Conference
              Jun 28-Jul 1, 2026
              Location: Atlanta, GA
            
            🔔 Reminders Active
            ✓ Competition Deadlines
            ✓ Meeting Notifications
            ✓ Registration Alerts
        """.trimIndent()
    }

    private fun showResources(textView: TextView) {
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

    private fun showNews(textView: TextView) {
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
            
            📱 Social Media Connected:
            ✓ Instagram - @fbla_chapter
            ✓ Twitter - @FBLAChapter
            ✓ Facebook - FBLA Central High
        """.trimIndent()
    }
}