package com.centrevillehs.fblaconnectv2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class PostNewsFragment : Fragment() {
    val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_post_news, container, false)

        val editNewsTitle = view.findViewById<EditText>(R.id.editNewsTitle)
        val editNewsContent = view.findViewById<EditText>(R.id.editNewsContent)
        val btnPostNews = view.findViewById<Button>(R.id.btnPostNews)

        btnPostNews.setOnClickListener {
            val title = editNewsTitle.text.toString().trim()
            val content = editNewsContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val docReference = db.collection("news").document()
                val prefs = requireActivity().getSharedPreferences("FBLAPrefs", AppCompatActivity.MODE_PRIVATE)
                val memberID = prefs.getString("memberID", "") ?: ""
//                val organizationId = db.collection("users").whereEqualTo("UserId", memberID).limit(1).get().result.documents[0].getString("OrganizationId") ?: ""
//                val organizationId =   db.collection("Organizations").whereArrayContains("adminIds", memberID).limit(1).get().result.documents[0].id
                db.collection("Organizations").whereArrayContains("adminIds", memberID).limit(1).get().addOnSuccessListener {
                    val organizationId = it.documents[0].id

                    val newsData = mapOf(
                        "userId" to memberID,
                        "title" to title,
                        "content" to content,
                        "createdAt" to FieldValue.serverTimestamp(),
                        "organizationId" to organizationId
                    )

                    docReference.set(newsData).addOnSuccessListener {
                        Log.d("NEWS_POST", "News created successfully")
                    }.addOnFailureListener { e ->
                        Log.e("NEWS_POST", "Failed to create news", e)
                    }
                }.addOnFailureListener {
                    Log.e("NEWS_POST", "Failed to update organizationId", it)
                }




                Toast.makeText(requireContext(), "News Posted: $title", Toast.LENGTH_LONG).show()
                
                // Return to Dashboard
                (activity as? MainActivity)?.showDashboardView()
            }
        }

        return view
    }
}