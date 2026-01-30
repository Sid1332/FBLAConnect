package com.centrevillehs.fblaconnectv2.ui.main

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.centrevillehs.fblaconnectv2.MainActivity

fun MainActivity.openUrl(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(this, "Opening: $url", Toast.LENGTH_SHORT).show()
    }
}
