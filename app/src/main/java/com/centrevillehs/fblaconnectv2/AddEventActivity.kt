package com.centrevillehs.fblaconnectv2

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
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
                // This is where you should upload the data to the database.
                Toast.makeText(requireContext(), "Successfully added event: $name, date: $date", Toast.LENGTH_LONG).show()
                // Return to Dashboard
                (activity as? MainActivity)?.showDashboardView()
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}