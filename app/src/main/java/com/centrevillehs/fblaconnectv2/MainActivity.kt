package com.centrevillehs.fblaconnectv2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.centrevillehs.fblaconnectv2.ui.main.openUrl
import com.centrevillehs.fblaconnectv2.ui.main.showEventsCalendar
import com.centrevillehs.fblaconnectv2.ui.main.showNews
import com.centrevillehs.fblaconnectv2.ui.main.showProfile
import com.centrevillehs.fblaconnectv2.ui.main.showResources
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var firstName = ""
    var lastName = ""
    var memberID = ""

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
}
