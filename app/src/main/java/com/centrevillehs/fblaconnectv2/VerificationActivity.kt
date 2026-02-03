package com.centrevillehs.fblaconnectv2

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class VerificationActivity : AppCompatActivity() {
    private lateinit var generatedCode: String
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var organizationId: String
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        // Get data from intent
        firstName = intent.getStringExtra("firstName") ?: ""
        lastName = intent.getStringExtra("lastName") ?: ""
        email = intent.getStringExtra("email") ?: ""
        password = intent.getStringExtra("password") ?: ""
        generatedCode = intent.getStringExtra("verificationCode") ?: ""
        organizationId = intent.getStringExtra("organizationId") ?: "6AzZYBOgkvqP3avwKtu2"

        val txtMessage = findViewById<TextView>(R.id.txtVerificationMessage)
        val editCode = findViewById<EditText>(R.id.editVerificationCode)
        val btnVerify = findViewById<Button>(R.id.btnVerify)
        val txtResend = findViewById<TextView>(R.id.txtResendCode)

        txtMessage.text = "We sent a verification code to:\n$email"

        // Verify button
        btnVerify.setOnClickListener {
            val enteredCode = editCode.text.toString()

            if (enteredCode.isEmpty()) {
                Toast.makeText(this, "Please enter the code", Toast.LENGTH_SHORT).show()
            } else if (enteredCode == generatedCode) {
                // Code is correct - save to Firebase
                saveUserToFirebase()
            } else {
                Toast.makeText(this, "Incorrect code. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        // Resend code
        txtResend.setOnClickListener {
            resendVerificationCode()
        }
    }

    private fun saveUserToFirebase() {
        val docReference = db.collection("users").document()
        val userId = docReference.id
        val userData = mapOf(
            "UserId" to userId,
            "OrganizationId" to organizationId,
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "password" to BCrypt.withDefaults().hashToString(12, password.toCharArray()),
            "createdAt" to FieldValue.serverTimestamp(),
        )

        docReference.set(userData)
            .addOnSuccessListener {
                Log.d("USER_REG", "User created successfully")

                // Save to SharedPreferences
                val prefs = getSharedPreferences("FBLAPrefs", MODE_PRIVATE)
                prefs.edit().apply {
                    putString("firstName", firstName)
                    putString("lastName", lastName)
                    putString("memberID", userId)
                    putString("email", email)
                    apply()
                }

                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("USER_REG", "Failed to create user", e)
                Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun resendVerificationCode() {
        generatedCode = EmailService.generateVerificationCode()

        lifecycleScope.launch {
            val success = EmailService.sendVerificationEmail(email, generatedCode)
            if (success) {
                Toast.makeText(this@VerificationActivity, "Code resent!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@VerificationActivity, "Failed to resend code", Toast.LENGTH_SHORT).show()
            }
        }
    }
}