package com.centrevillehs.fblaconnectv2.ui.main

import android.view.View
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.centrevillehs.fblaconnectv2.MainActivity

fun MainActivity.showEventsCalendar(
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
