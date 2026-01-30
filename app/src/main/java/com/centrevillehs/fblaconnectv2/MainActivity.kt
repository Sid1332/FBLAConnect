package com.centrevillehs.fblaconnectv2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.centrevillehs.fblaconnectv2.ui.main.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var firstName = ""
    var lastName = ""
    var memberID = ""

    private lateinit var dashboardView: ScrollView
    private lateinit var calendarContainer: LinearLayout
    private lateinit var contentArea: ScrollView
    private lateinit var contentText: TextView
    private lateinit var socialMediaButtons: LinearLayout
    private lateinit var calendarView: CalendarView
    private lateinit var eventDetailsText: TextView

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

        // Initialize Views
        dashboardView = findViewById(R.id.dashboardView)
        calendarContainer = findViewById(R.id.calendarContainer)
        contentArea = findViewById(R.id.contentArea)
        contentText = findViewById(R.id.contentText)
        socialMediaButtons = findViewById(R.id.socialMediaButtons)
        calendarView = findViewById(R.id.calendarView)
        eventDetailsText = findViewById(R.id.eventDetailsText)

        // Setup Header
        val txtUserName = findViewById<TextView>(R.id.txtUserName)
        txtUserName.text = "$firstName $lastName"

        // Setup Category Click Listeners
        findViewById<LinearLayout>(R.id.categoryRegistration).setOnClickListener {
            showProfileView()
        }
        findViewById<LinearLayout>(R.id.categoryCompetition).setOnClickListener {
            openUrl("https://www.fbla.org/competitions/")
        }
        findViewById<LinearLayout>(R.id.categoryEvents).setOnClickListener {
            showEventsView()
        }
        findViewById<LinearLayout>(R.id.categoryNews).setOnClickListener {
            showNewsView()
        }
        findViewById<LinearLayout>(R.id.categoryResources).setOnClickListener {
            showResourcesView()
        }
        findViewById<LinearLayout>(R.id.categorySocial).setOnClickListener {
            openUrl("https://www.fbla.org/social-media/")
        }

        // Setup Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> showDashboardView()
                R.id.nav_calendar -> showEventsView()
                R.id.nav_profile -> showProfileView()
            }
            true
        }

        // Social media buttons in contentArea
        findViewById<View>(R.id.btnInstagram).setOnClickListener { openUrl("https://www.instagram.com/fbla_national/") }
        findViewById<View>(R.id.btnTwitter).setOnClickListener { openUrl("https://x.com/FBLA_National") }
        findViewById<View>(R.id.btnFacebook).setOnClickListener { openUrl("https://www.facebook.com/FutureBusinessLeaders/") }

        // Start with Dashboard
        showDashboardView()
    }

    private fun hideAll() {
        dashboardView.visibility = View.GONE
        calendarContainer.visibility = View.GONE
        contentArea.visibility = View.GONE
    }

    private fun showDashboardView() {
        hideAll()
        dashboardView.visibility = View.VISIBLE
    }

    private fun showProfileView() {
        hideAll()
        showProfile(contentText, socialMediaButtons, contentArea, calendarContainer)
    }

    private fun showEventsView() {
        hideAll()
        showEventsCalendar(calendarView, eventDetailsText, contentArea, calendarContainer)
    }

    private fun showNewsView() {
        hideAll()
        showNews(contentText, socialMediaButtons, contentArea, calendarContainer)
    }

    private fun showResourcesView() {
        hideAll()
        showResources(contentText, socialMediaButtons, contentArea, calendarContainer)
    }
}