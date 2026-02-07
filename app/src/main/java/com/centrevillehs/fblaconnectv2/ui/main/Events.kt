package com.centrevillehs.fblaconnectv2.ui.main

import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.centrevillehs.fblaconnectv2.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

fun MainActivity.showEventsCalendar(
    calendarView: CalendarView,
    eventText: TextView,
    contentArea: ScrollView,
    calendarContainer: LinearLayout
) {
    // Hide text content, show calendar
    contentArea.visibility = View.GONE
    calendarContainer.visibility = View.VISIBLE

    val db = FirebaseFirestore.getInstance()

    // Get user id from MainActivity's property
    val currentUserId = memberID

    db.collection("users").document(currentUserId).get()
        .addOnSuccessListener { userDoc ->
            if (!userDoc.exists()) {
                Log.d("EVENTS", "User document does not exist for ID: $currentUserId")
                return@addOnSuccessListener
            }

            val organizationId = userDoc.getString("OrganizationId") ?: ""
            Log.d("EVENTS", "User Org ID: $organizationId")

            db.collection("Organizations").document(organizationId).get()
                .addOnSuccessListener { orgDoc ->
                    val orgsToFetch = mutableListOf<String>()

                    if (organizationId.isNotEmpty()) {
                        orgsToFetch.add(organizationId)
                    }

                    val path = orgDoc.get("path") as? List<String> ?: listOf()
                    orgsToFetch.addAll(path.filter { it.isNotEmpty() })
                    
                    Log.d("EVENTS", "Orgs to fetch news/events from: $orgsToFetch")

                    if (orgsToFetch.isEmpty()) {
                        eventText.text = "👆 Select a date to view events"
                        return@addOnSuccessListener
                    }

                    // FIXED: Changed "organization" to "organizationId" to match AddEventFragment
                    db.collection("events").whereIn("organizationId", orgsToFetch).get().addOnSuccessListener { eventsDoc ->
                        Log.d("EVENTS", "Events found: ${eventsDoc.size()}")
                        val eventsMap = mutableMapOf<String, String>()

                        for (event in eventsDoc) {
                            val date = event.getString("date") ?: ""
                            val name = event.getString("name") ?: ""
                            val description = event.getString("description") ?: ""
                            if (date.isNotEmpty()) {
                                eventsMap[date] = "$name\n\n$description"
                            }

                            Log.d("EVENTS", "date: $date, name: $name, description: $description")
                        }

                        // Set default message
                        eventText.text = "👆 Select a date to view events"

                        // Handle date selection
                        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                            // Format date to match the picker: MM/DD/YYYY
                            val dateKey = String.format("%02d/%02d/%d", month + 1, dayOfMonth, year)
                            Log.d("EVENTS", "Selected date: $dateKey")

                            eventText.text = if (eventsMap.containsKey(dateKey)) {
                                eventsMap[dateKey]
                            } else {
                                """
                                    📅 $dateKey
                                    
                                    No events scheduled for this date.
                                    
                                    💡 Tap on an event date above to see details!
                                """.trimIndent()
                            }
                        }
                    }.addOnFailureListener { e ->
                        Log.e("EVENTS", "Error fetching events", e)
                    }
                }.addOnFailureListener { e ->
                    Log.e("EVENTS", "Error fetching organization", e)
                }
        }.addOnFailureListener { e ->
            Log.e("EVENTS", "Error fetching user", e)
        }
}
