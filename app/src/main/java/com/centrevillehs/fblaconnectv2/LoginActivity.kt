package com.centrevillehs.fblaconnectv2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editFirstName = findViewById<EditText>(R.id.editFirstName)
        val editLastName = findViewById<EditText>(R.id.editLastName)
        val editMemberID = findViewById<EditText>(R.id.editMemberID)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val firstName = editFirstName.text.toString()
            val lastName = editLastName.text.toString()
            val memberID = editMemberID.text.toString()

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && memberID.isNotEmpty()) {
                // Save to SharedPreferences
                val prefs = getSharedPreferences("FBLAPrefs", MODE_PRIVATE)
                prefs.edit().apply {
                    putString("firstName", firstName)
                    putString("lastName", lastName)
                    putString("memberID", memberID)
                    apply()
                }

                // Go to main app
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}