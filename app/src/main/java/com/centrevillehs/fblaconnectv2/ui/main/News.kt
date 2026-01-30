package com.centrevillehs.fblaconnectv2.ui.main

import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.centrevillehs.fblaconnectv2.MainActivity

fun MainActivity.showNews(
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
