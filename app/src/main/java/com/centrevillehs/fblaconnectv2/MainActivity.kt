package com.centrevillehs.fblaconnectv2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.centrevillehs.fblaconnectv2.ui.main.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    var firstName = ""
    var lastName = ""
    var memberID = ""
    var isAdmin = false

    private lateinit var headerLayout: LinearLayout
    private lateinit var dashboardView: ScrollView
    private lateinit var calendarContainer: LinearLayout
    private lateinit var contentArea: ScrollView
    private lateinit var contentText: TextView
    private lateinit var socialMediaButtons: LinearLayout
    private lateinit var calendarView: CalendarView
    private lateinit var eventDetailsText: TextView
    private lateinit var fragmentContainer: FrameLayout

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
        headerLayout = findViewById(R.id.headerLayout)
        dashboardView = findViewById(R.id.dashboardView)
        calendarContainer = findViewById(R.id.calendarContainer)
        contentArea = findViewById(R.id.contentArea)
        contentText = findViewById(R.id.contentText)
        socialMediaButtons = findViewById(R.id.socialMediaButtons)
        calendarView = findViewById(R.id.calendarView)
        eventDetailsText = findViewById(R.id.eventDetailsText)
        fragmentContainer = findViewById(R.id.fragmentContainer)

        // Setup Header
        val txtUserName = findViewById<TextView>(R.id.txtUserName)
        txtUserName.text = "$firstName $lastName"

        // Setup Category Click Listeners
        findViewById<LinearLayout>(R.id.categoryRegistration).setOnClickListener {
            showProfileView()
        }
        findViewById<LinearLayout>(R.id.categoryCompetition).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.fbla.org/competitions/"))
            startActivity(intent)
        }
        findViewById<LinearLayout>(R.id.categoryEvents).setOnClickListener {
            showEventsView()
        }
        findViewById<LinearLayout>(R.id.categoryNews).setOnClickListener {
            showNewsListView()
        }
        findViewById<LinearLayout>(R.id.categoryResources).setOnClickListener {
            showResourcesView()
        }
        findViewById<LinearLayout>(R.id.categorySocial).setOnClickListener {
            showSocialPopup()
        }

        // Admin Section
        val categoryAddEvent = findViewById<LinearLayout>(R.id.categoryAddEvent)
        val categoryPostNews = findViewById<LinearLayout>(R.id.categoryPostNews)

        val db = FirebaseFirestore.getInstance()
        val userDocument = db.collection("users").document(memberID)

        userDocument.get().addOnSuccessListener { doc ->
            val organizationId = doc.getString("OrganizationId") ?: return@addOnSuccessListener
            val organizationDocument = db.collection("Organizations").document(organizationId)
            
            organizationDocument.get().addOnSuccessListener { orgDoc ->
                if (!orgDoc.exists()) return@addOnSuccessListener
                val administrators = orgDoc.get("adminIds") as? List<String> ?: emptyList()
                
                if (memberID in administrators) {
                    isAdmin = true
                    categoryAddEvent.visibility = View.VISIBLE
                    categoryPostNews.visibility = View.VISIBLE
                    
                    categoryAddEvent.setOnClickListener { showAddEventView() }
                    categoryPostNews.setOnClickListener { showPostNewsView() }
                } else {
                    categoryAddEvent.visibility = View.GONE
                    categoryPostNews.visibility = View.GONE
                }
            }
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

        // Start with Dashboard
        showDashboardView()
    }

    private fun showSocialPopup() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_social_media, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.btnDialogInstagram).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.instagram.com/fbla_national/"))
            startActivity(intent)
            dialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btnDialogTwitter).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://x.com/FBLA_National"))
            startActivity(intent)
            dialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btnDialogFacebook).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.facebook.com/FutureBusinessLeaders/"))
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun hideAll() {
        headerLayout.visibility = View.VISIBLE
        dashboardView.visibility = View.GONE
        calendarContainer.visibility = View.GONE
        contentArea.visibility = View.GONE
        fragmentContainer.visibility = View.GONE
    }

    fun showDashboardView() {
        hideAll()
        dashboardView.visibility = View.VISIBLE
    }

    fun showProfileView() {
        hideAll()
        headerLayout.visibility = View.GONE
        fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ProfileFragment())
            .commit()
    }

    fun showEventsView() {
        hideAll()
        showEventsCalendar(calendarView, eventDetailsText, contentArea, calendarContainer)
    }

    fun showNewsListView() {
        hideAll()
        fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, NewsListFragment())
            .commit()
    }

    fun showResourcesView() {
        hideAll()
        fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ResourcesFragment())
            .commit()
    }

    fun showAddEventView() {
        hideAll()
        fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AddEventFragment())
            .commit()
    }

    fun showPostNewsView() {
        hideAll()
        fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PostNewsFragment())
            .commit()
    }
}