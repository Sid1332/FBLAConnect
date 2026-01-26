package com.centrevillehs.fblaconnectv2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
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
        showProfile(contentText, socialMediaButtons)

        // Handle bottom nav clicks
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                1 -> showProfile(contentText, socialMediaButtons)
                2 -> showEvents(contentText, socialMediaButtons)
                3 -> showResources(contentText, socialMediaButtons)
                4 -> showNews(contentText, socialMediaButtons)
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

    private fun showProfile(textView: TextView, socialButtons: LinearLayout) {
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

    private fun showEvents(textView: TextView, socialButtons: LinearLayout) {
        socialButtons.visibility = View.GONE
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

    private fun showResources(textView: TextView, socialButtons: LinearLayout) {
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

    private fun showNews(textView: TextView, socialButtons: LinearLayout) {
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