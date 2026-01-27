package com.centrevillehs.fblaconnectv2

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val iconTogglePassword = findViewById<ImageView>(R.id.iconTogglePassword)
        val txtForgotPassword = findViewById<TextView>(R.id.txtForgotPassword)
        val txtSignUp = findViewById<TextView>(R.id.txtSignUp)

        // Toggle password visibility
        iconTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                editPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                editPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            editPassword.setSelection(editPassword.text.length)
        }

        // Login button click
        btnLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Extract first name from email (before @)
                val firstName = email.substringBefore("@").capitalize()

                // Save to SharedPreferences
                val prefs = getSharedPreferences("FBLAPrefs", MODE_PRIVATE)
                prefs.edit().apply {
                    putString("firstName", firstName)
                    putString("lastName", "Member")
                    putString("memberID", "12345")
                    putString("email", email)
                    apply()
                }

                // Go to main app
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Forgot password click (placeholder)
        txtForgotPassword.setOnClickListener {
            Toast.makeText(this, "Password reset coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Sign up click (placeholder)
        txtSignUp.setOnClickListener {
            Toast.makeText(this, "Sign up coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}