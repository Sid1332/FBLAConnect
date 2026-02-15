package com.centrevillehs.fblaconnectv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val profileImageLetter = view.findViewById<TextView>(R.id.profileImageLetter)
        val profileName = view.findViewById<TextView>(R.id.profileName)
        val profileEmail = view.findViewById<TextView>(R.id.profileEmail)
        val editProfileButton = view.findViewById<RelativeLayout>(R.id.editProfileButton)
        val signOutButton = view.findViewById<RelativeLayout>(R.id.signOutButton)
        val languageButton = view.findViewById<RelativeLayout>(R.id.languageButton)
        val aboutButton = view.findViewById<RelativeLayout>(R.id.aboutButton)
        val termsButton = view.findViewById<RelativeLayout>(R.id.termsButton)
        val rateButton = view.findViewById<RelativeLayout>(R.id.rateButton)
        val shareButton = view.findViewById<RelativeLayout>(R.id.shareButton)

        val prefs = requireActivity().getSharedPreferences("FBLAPrefs", Context.MODE_PRIVATE)
        val firstName = prefs.getString("firstName", "")
        val lastName = prefs.getString("lastName", "")
        val email = prefs.getString("email", "")

        if (!firstName.isNullOrEmpty()) {
            profileImageLetter.text = firstName.take(1).uppercase()
        }

        if (!firstName.isNullOrEmpty() && !lastName.isNullOrEmpty()) {
            profileName.text = getString(R.string.user_full_name, firstName, lastName)
        } else {
            profileName.text = getString(R.string.profile_name_placeholder)
        }

        if (!email.isNullOrEmpty()) {
            profileEmail.text = email
        }

        editProfileButton.setOnClickListener {
            Toast.makeText(context, R.string.edit_profile_clicked, Toast.LENGTH_SHORT).show()
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