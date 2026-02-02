package com.centrevillehs.fblaconnectv2

import android.app.DatePickerDialog
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/**
 * Note: Despite the "Activity" name for consistency, this class is a Fragment
 * to allow it to be hosted inside MainActivity while keeping the top/bottom bars.
 */
class AddEventActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val db = FirebaseFirestore.getInstance()
        val view = inflater.inflate(R.layout.activity_add_event, container, false)

        val editEventName = view.findViewById<EditText>(R.id.editEventName)
        val editEventDate = view.findViewById<EditText>(R.id.editEventDate)
        val editEventDescription = view.findViewById<EditText>(R.id.editEventDescription)
        val btnSaveEvent = view.findViewById<Button>(R.id.btnSaveEvent)

        // Setup Date Picker
        editEventDate.isFocusable = false
        editEventDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    editEventDate.setText(String.format("%02d/%02d/%d", month + 1, day, year))
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        btnSaveEvent.setOnClickListener {
            val name = editEventName.text.toString().trim()
            val date = editEventDate.text.toString().trim()
            val description = editEventDescription.text.toString().trim()

            if (name.isNotEmpty() && date.isNotEmpty() && description.isNotEmpty()) {
                var firstName = ""
                var lastName = ""
                var memberID = ""
                val prefs = requireContext().getSharedPreferences("FBLAPrefs", MODE_PRIVATE)
                memberID = prefs.getString("memberID", "") ?: ""
                // This is where you should upload the data to the database.
                db.collection("users").document(memberID).get().addOnSuccessListener { //Gets the user organization Id
                    doc ->
                    val organizationId = doc.getString("OrganizationId")


                    val userData = mapOf(
                        "name" to name,
                        "date" to date,
                        "description" to description,
                        "organizationId" to organizationId,
                        "userId" to memberID,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    db.collection("events").add(userData)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Successfully added event: $name, date: $date", Toast.LENGTH_LONG).show()
                            // Return to Dashboard

                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed to add event", Toast.LENGTH_SHORT).show()
                        }

                }
                (activity as? MainActivity)?.showDashboardView()




            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}