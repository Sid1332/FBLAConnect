package com.centrevillehs.fblaconnectv2.ui.main

import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.centrevillehs.fblaconnectv2.MainActivity

fun MainActivity.showResources(
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
