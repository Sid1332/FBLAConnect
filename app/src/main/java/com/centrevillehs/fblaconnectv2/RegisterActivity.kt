package com.centrevillehs.fblaconnectv2

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.snap
import com.google.firebase.firestore.FieldValue
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private var passwordVisible = false
    private var confirmPasswordVisible = false

    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val editFirstName = findViewById<EditText>(R.id.editFirstName)
        val editLastName = findViewById<EditText>(R.id.editLastName)
        val editEmail = findViewById<EditText>(R.id.editRegisterEmail)
        val editPassword = findViewById<EditText>(R.id.editRegisterPassword)
        val editConfirmPassword = findViewById<EditText>(R.id.editConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val txtSignIn = findViewById<TextView>(R.id.txtSignIn)
        val iconTogglePassword = findViewById<ImageView>(R.id.iconToggleRegisterPassword)
        val iconToggleConfirm = findViewById<ImageView>(R.id.iconToggleConfirmPassword)

        // Toggle password visibility
        iconTogglePassword.setOnClickListener {
            passwordVisible = !passwordVisible
            if (passwordVisible) {
                editPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                editPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            editPassword.setSelection(editPassword.text.length)
        }

        // Toggle confirm password visibility
        iconToggleConfirm.setOnClickListener {
            confirmPasswordVisible = !confirmPasswordVisible
            if (confirmPasswordVisible) {
                editConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                editConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            editConfirmPassword.setSelection(editConfirmPassword.text.length)
        }

        // Register button
        btnRegister.setOnClickListener {
            val firstName = editFirstName.text.toString().trim()
            val lastName = editLastName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString()
            val confirmPassword = editConfirmPassword.text.toString()

            // Validation
            when {
                firstName.isEmpty() -> {
                    Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show()
                }
                lastName.isEmpty() -> {
                    Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show()
                }
                email.isEmpty() -> {
                    Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 -> {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                }
                confirmPassword.isEmpty() -> {
                    Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show()
                }
                password != confirmPassword -> {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    db.collection("users")
                        .whereEqualTo("email",email)
                        .get() //Initiates the query with the options defined above( look for the email in side of the users collection )
                        .addOnSuccessListener { snapshot ->
                            if (snapshot.size() > 0){
                                Toast.makeText(this, "Account already exists", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                val docReference = db.collection("users").document()
                                val userId = docReference.id
                                val userData = mapOf(
                                    "UserId" to userId,
                                    "OrganizationId" to "6AzZYBOgkvqP3avwKtu2",
                                    "firstName" to firstName,
                                    "lastName" to lastName,
                                    "email" to email,
                                    "password" to BCrypt.withDefaults().hashToString(12, password.toCharArray()),
                                    "createdAt" to FieldValue.serverTimestamp(),
                                )
                                docReference
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Log.d("USER_REG", "User created successfully")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("USER_REG", "Failed to create user", e)
                                    }
                                // Registration successful - save to SharedPreferences
                                val prefs = getSharedPreferences("FBLAPrefs", MODE_PRIVATE)
                                prefs.edit().apply {
                                    putString("firstName", firstName)
                                    putString("lastName", lastName)
                                    putString("memberID", userId)
                                    putString("email", email)
                                    apply()
                                }

                                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                // Go to main app
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }


                }
            }
        }

        // Sign in link - go to login page
        txtSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}