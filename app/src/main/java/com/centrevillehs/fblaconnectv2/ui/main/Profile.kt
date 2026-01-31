package com.centrevillehs.fblaconnectv2.ui.main

import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.centrevillehs.fblaconnectv2.MainActivity

fun MainActivity.showProfile(
    textView: TextView,
    contentArea: ScrollView,
    calendarContainer: LinearLayout
) {
    // Show text content, hide calendar
    contentArea.visibility = View.VISIBLE
    calendarContainer.visibility = View.GONE

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
    """.trimIndent()
}
