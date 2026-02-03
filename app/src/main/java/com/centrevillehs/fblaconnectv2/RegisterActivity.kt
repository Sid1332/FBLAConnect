package com.centrevillehs.fblaconnectv2

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private var passwordVisible = false
    private var confirmPasswordVisible = false

    val db = FirebaseFirestore.getInstance()
    private val organizationsList = mutableListOf<Pair<String, String>>() // Name to ID
    private var selectedOrganizationId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val editFirstName = findViewById<EditText>(R.id.editFirstName)
        val editLastName = findViewById<EditText>(R.id.editLastName)
        val editEmail = findViewById<EditText>(R.id.editRegisterEmail)
        val autoCompleteOrganization = findViewById<AutoCompleteTextView>(R.id.autoCompleteOrganization)
        val editPassword = findViewById<EditText>(R.id.editRegisterPassword)
        val editConfirmPassword = findViewById<EditText>(R.id.editConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val txtSignIn = findViewById<TextView>(R.id.txtSignIn)
        val iconTogglePassword = findViewById<ImageView>(R.id.iconToggleRegisterPassword)
        val iconToggleConfirm = findViewById<ImageView>(R.id.iconToggleConfirmPassword)

        setupOrganizationAutoComplete(autoCompleteOrganization)

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
            val typedOrgName = autoCompleteOrganization.text.toString().trim()

            // Verify if the typed organization name matches a known one
            val matchingOrg = organizationsList.find { it.first.equals(typedOrgName, ignoreCase = true) }
            selectedOrganizationId = matchingOrg?.second ?: ""

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
                selectedOrganizationId.isEmpty() -> {
                    Toast.makeText(this, "Please select a valid organization from the list", Toast.LENGTH_SHORT).show()
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
                    // Check if email already exists
                    db.collection("users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            if (snapshot.size() > 0) {
                                Toast.makeText(this, "Account already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                // Generate and send verification code
                                val verificationCode = EmailService.generateVerificationCode()

                                // Disable button to prevent double-clicks
                                btnRegister.isEnabled = false

                                lifecycleScope.launch {
                                    Toast.makeText(this@RegisterActivity, "Sending verification code...", Toast.LENGTH_SHORT).show()

                                    val success = EmailService.sendVerificationEmail(email, verificationCode)

                                    // Re-enable button
                                    btnRegister.isEnabled = true

                                    if (success) {
                                        Toast.makeText(this@RegisterActivity, "Verification code sent to $email", Toast.LENGTH_LONG).show()

                                        // Go to verification screen
                                        val intent = Intent(this@RegisterActivity, VerificationActivity::class.java).apply {
                                            putExtra("firstName", firstName)
                                            putExtra("lastName", lastName)
                                            putExtra("email", email)
                                            putExtra("password", password)
                                            putExtra("organizationId", selectedOrganizationId)
                                            putExtra("verificationCode", verificationCode)
                                        }
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "Failed to send email. Please check your internet connection and try again.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("USER_REG", "Failed to check email", e)
                            Toast.makeText(this, "Error checking email. Please try again.", Toast.LENGTH_SHORT).show()
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

    private fun setupOrganizationAutoComplete(autoComplete: AutoCompleteTextView) {
        db.collection("Organizations")
            .whereEqualTo("level", 4)
            .get()
            .addOnSuccessListener { documents ->
                organizationsList.clear()
                val names = mutableListOf<String>()
                
                for (document in documents) {
                    val name = document.getString("name") ?: "Unknown"
                    organizationsList.add(name to document.id)
                    names.add(name)
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, names)
                autoComplete.setAdapter(adapter)
                
                // Set click listener to capture ID when selected from dropdown
                autoComplete.setOnItemClickListener { parent, _, position, _ ->
                    val selectedName = parent.getItemAtPosition(position) as String
                    val matchingOrg = organizationsList.find { it.first == selectedName }
                    selectedOrganizationId = matchingOrg?.second ?: ""
                }
            }
            .addOnFailureListener { e ->
                Log.e("ORG_FETCH", "Failed to fetch organizations", e)
                Toast.makeText(this, "Failed to load organizations", Toast.LENGTH_SHORT).show()
            }
    }
}