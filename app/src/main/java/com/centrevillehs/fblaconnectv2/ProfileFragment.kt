package com.centrevillehs.fblaconnectv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

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

        if (!firstName.isNullOrEmpty() && !lastName.isNullOrEmpty()) {
            profileName.text = "$firstName $lastName"
        } else {
            profileName.text = "John Pork"
        }

        if (!email.isNullOrEmpty()) {
            profileEmail.text = email
        }

        editProfileButton.setOnClickListener {
            Toast.makeText(context, "Edit Profile Clicked", Toast.LENGTH_SHORT).show()
        }

        signOutButton.setOnClickListener {
            val editor = prefs.edit()
            editor.clear()
            editor.apply()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        languageButton.setOnClickListener {
            Toast.makeText(context, "Language Clicked", Toast.LENGTH_SHORT).show()
        }

        aboutButton.setOnClickListener {
            showAboutDialog()
        }

        termsButton.setOnClickListener {
            showTermsDialog()
        }

        rateButton.setOnClickListener {
            Toast.makeText(context, "Rate this App Clicked", Toast.LENGTH_SHORT).show()
        }

        shareButton.setOnClickListener {
            Toast.makeText(context, "Share this App Clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("About")
            .setMessage("This is a an app for FBLA.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showTermsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Terms & Conditions")
            .setMessage("These are the terms and conditions for using this app. By using this app, you agree to these terms.")
            .setPositiveButton("OK", null)
            .show()
    }
}