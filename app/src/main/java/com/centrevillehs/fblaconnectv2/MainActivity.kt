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
import kotlin.collections.emptyList

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
            showSocialPopup()
        }

        // Admin Only Section
        val categoryAddEvent = findViewById<LinearLayout>(R.id.categoryAddEvent)

        val db = FirebaseFirestore.getInstance()
        val userDocument = db.collection("users").document(memberID)

        userDocument.get().addOnSuccessListener { doc ->
            val organizationId = doc.getString("OrganizationId")?: return@addOnSuccessListener //Guarantees a string and stops android studio from yelling at me.


            val organizationDocument = db.collection("Organizations").document(organizationId)
            //Get the administrators and compare to the current user
            organizationDocument.get().addOnSuccessListener { orgDoc ->
                if (!orgDoc.exists()) return@addOnSuccessListener

                val administrators = orgDoc.get("adminIds") as? List<String> ?: emptyList() // Get a string array, if not make an empty list (prevents errors)
                Log.d(
                    "debug",
                    "equal=${administrators[0] == memberID}, " +
                            "adminLen=${administrators[0].length}, memberLen=${memberID.length}"
                )
                if (memberID in administrators){
                    isAdmin = true
                }
                if (isAdmin) {
                    categoryAddEvent.visibility = View.VISIBLE
                    categoryAddEvent.setOnClickListener {
                        showAddEventView()
                    }
                } else {
                    categoryAddEvent.visibility = View.GONE
                }
            }


        }


        // Logout Button
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val editor = getSharedPreferences("FBLAPrefs", MODE_PRIVATE).edit()
            editor.clear()
            editor.apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
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
            openUrl("https://www.instagram.com/fbla_national/")
            dialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btnDialogTwitter).setOnClickListener {
            openUrl("https://x.com/FBLA_National")
            dialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btnDialogFacebook).setOnClickListener {
            openUrl("https://www.facebook.com/FutureBusinessLeaders/")
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

    fun showNewsView() {
        hideAll()
        showNews(contentText, socialMediaButtons, contentArea, calendarContainer)
    }

    fun showResourcesView() {
        hideAll()
        showResources(contentText, socialMediaButtons, contentArea, calendarContainer)
    }

    fun showAddEventView() {
        hideAll()
        fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AddEventActivity())
            .commit()
    }
}