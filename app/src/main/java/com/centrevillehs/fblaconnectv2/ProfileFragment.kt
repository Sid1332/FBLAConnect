package com.centrevillehs.fblaconnectv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var profileImageLetter: TextView
    private lateinit var profileName: TextView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileImageLetter = view.findViewById<TextView>(R.id.profileImageLetter)
        profileName = view.findViewById<TextView>(R.id.profileName)
        val profileEmail = view.findViewById<TextView>(R.id.profileEmail)
        val editProfileButton = view.findViewById<RelativeLayout>(R.id.editProfileButton)
        val signOutButton = view.findViewById<RelativeLayout>(R.id.signOutButton)
        val languageButton = view.findViewById<RelativeLayout>(R.id.languageButton)
        val aboutButton = view.findViewById<RelativeLayout>(R.id.aboutButton)
        val termsButton = view.findViewById<RelativeLayout>(R.id.termsButton)
        val rateButton = view.findViewById<RelativeLayout>(R.id.rateButton)
        val shareButton = view.findViewById<RelativeLayout>(R.id.shareButton)

        updateProfileInfo()

        val prefs = requireActivity().getSharedPreferences("FBLAPrefs", Context.MODE_PRIVATE)
        val email = prefs.getString("email", "")
        if (!email.isNullOrEmpty()) {
            profileEmail.text = email
        }

        editProfileButton.setOnClickListener {
            showEditProfileDialog()
        }

        signOutButton.setOnClickListener {
            prefs.edit {
                clear()
            }
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        languageButton.setOnClickListener {
            Toast.makeText(context, R.string.language_clicked, Toast.LENGTH_SHORT).show()
        }

        aboutButton.setOnClickListener {
            showAboutDialog()
        }

        termsButton.setOnClickListener {
            showTermsDialog()
        }

        rateButton.setOnClickListener {
            Toast.makeText(context, R.string.rate_app_clicked, Toast.LENGTH_SHORT).show()
        }

        shareButton.setOnClickListener {
            Toast.makeText(context, R.string.share_app_clicked, Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun updateProfileInfo() {
        val prefs = requireActivity().getSharedPreferences("FBLAPrefs", Context.MODE_PRIVATE)
        val firstName = prefs.getString("firstName", "")
        val lastName = prefs.getString("lastName", "")

        if (!firstName.isNullOrEmpty()) {
            profileImageLetter.text = firstName.take(1).uppercase()
        }

        if (!firstName.isNullOrEmpty() && !lastName.isNullOrEmpty()) {
            profileName.text = getString(R.string.user_full_name, firstName, lastName)
        } else {
            profileName.text = getString(R.string.profile_name_placeholder)
        }
    }

    private fun showEditProfileDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile, null)
        val editFirstName = dialogView.findViewById<EditText>(R.id.editFirstName)
        val editLastName = dialogView.findViewById<EditText>(R.id.editLastName)

        val prefs = requireActivity().getSharedPreferences("FBLAPrefs", Context.MODE_PRIVATE)
        editFirstName.setText(prefs.getString("firstName", ""))
        editLastName.setText(prefs.getString("lastName", ""))

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Profile")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newFirstName = editFirstName.text.toString()
                val newLastName = editLastName.text.toString()
                val memberID = prefs.getString("memberID", "")

                if (memberID.isNullOrEmpty()) {
                    Toast.makeText(context, "Error: User not logged in.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val userUpdates = mapOf(
                    "firstName" to newFirstName,
                    "lastName" to newLastName
                )

                db.collection("users").document(memberID)
                    .update(userUpdates)
                    .addOnSuccessListener {
                        prefs.edit {
                            putString("firstName", newFirstName)
                            putString("lastName", newLastName)
                        }
                        updateProfileInfo()
                        (activity as? MainActivity)?.updateHeader()
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error updating profile: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.about_title)
            .setMessage(R.string.about_message)
            .setPositiveButton(R.string.ok, null)
            .show()
    }

    private fun showTermsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.terms_title)
            .setMessage(R.string.terms_message)
            .setPositiveButton(R.string.ok, null)
            .show()
    }
}
