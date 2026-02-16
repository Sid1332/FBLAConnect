package com.centrevillehs.fblaconnectv2.ui.main

import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.centrevillehs.fblaconnectv2.Event
import com.centrevillehs.fblaconnectv2.EventAdapter
import com.centrevillehs.fblaconnectv2.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

fun MainActivity.showEventsCalendar(
    calendarView: CalendarView,
    noEventsText: TextView,
    recyclerView: RecyclerView,
    calendarContainer: LinearLayout
) {
    calendarContainer.visibility = View.VISIBLE
    
    val db = FirebaseFirestore.getInstance()
    val eventsList = mutableListOf<Event>()
    val currentUserId = memberID

    db.collection("users").document(currentUserId).get()
        .addOnSuccessListener { userDoc ->
            if (!userDoc.exists()) return@addOnSuccessListener

            val organizationId = userDoc.getString("OrganizationId") ?: ""
            db.collection("Organizations").document(organizationId).get()
                .addOnSuccessListener { orgDoc ->
                    val orgsToFetch = mutableListOf<String>()
                    if (organizationId.isNotEmpty()) orgsToFetch.add(organizationId)
                    val path = orgDoc.get("path") as? List<String> ?: listOf()
                    orgsToFetch.addAll(path.filter { it.isNotEmpty() })

                    if (orgsToFetch.isEmpty()) return@addOnSuccessListener

                    db.collection("events").whereIn("organizationId", orgsToFetch).get().addOnSuccessListener { eventsDoc ->
                        val allEvents = mutableListOf<Event>()
                        for (doc in eventsDoc) {
                            allEvents.add(Event(
                                id = doc.id,
                                name = doc.getString("name") ?: "",
                                date = doc.getString("date") ?: "",
                                description = doc.getString("description") ?: ""
                            ))
                        }

                        recyclerView.layoutManager = LinearLayoutManager(this)
                        
                        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                            val selectedDate = String.format("%02d/%02d/%d", month + 1, dayOfMonth, year)
                            val filteredEvents = allEvents.filter { it.date == selectedDate }

                            if (filteredEvents.isEmpty()) {
                                noEventsText.visibility = View.VISIBLE
                                recyclerView.visibility = View.GONE
                                noEventsText.text = "📅 $selectedDate\n\nNo events scheduled for this date."
                            } else {
                                noEventsText.visibility = View.GONE
                                recyclerView.visibility = View.VISIBLE
                                recyclerView.adapter = EventAdapter(filteredEvents)
                            }
                        }
                    }
                }
        }
}
