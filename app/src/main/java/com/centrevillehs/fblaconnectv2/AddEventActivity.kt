package com.centrevillehs.fblaconnectv2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        val editEventName = findViewById<EditText>(R.id.editEventName)
        val editEventDate = findViewById<EditText>(R.id.editEventDate)
        val editEventDescription = findViewById<EditText>(R.id.editEventDescription)
        val btnSaveEvent = findViewById<Button>(R.id.btnSaveEvent)

        btnSaveEvent.setOnClickListener {
            val name = editEventName.text.toString().trim()
            val date = editEventDate.text.toString().trim()
            val description = editEventDescription.text.toString().trim()

            if (name.isEmpty() || date.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // In a real app, you'd save this to a database
                Toast.makeText(this, "Event '$name' saved!", Toast.LENGTH_SHORT).show()
                finish() // Go back to home
            }
        }
    }
}