package com.centrevillehs.fblaconnectv2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

class NewsListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private val newsList = mutableListOf<NewsPost>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewNews)
        recyclerView.layoutManager = LinearLayoutManager(context)
        newsAdapter = NewsAdapter(newsList)
        recyclerView.adapter = newsAdapter

        fetchNews()

        return view
    }

    private fun fetchNews() {
        val currentUserId = requireActivity().getSharedPreferences("FBLAPrefs", AppCompatActivity.MODE_PRIVATE).getString("memberID", "") ?: ""
        
        db.collection("users").document(currentUserId).get()
            .addOnSuccessListener { userDoc ->
                if (!userDoc.exists()) {
                    Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val organizationId = userDoc.getString("OrganizationId") ?: ""
                
                db.collection("Organizations").document(organizationId).get()
                    .addOnSuccessListener { orgDoc ->
                        val orgsToFetch = mutableListOf<String>()
                        
                        if (organizationId.isNotEmpty()) {
                            orgsToFetch.add(organizationId)
                        }

                        val path = orgDoc.get("path") as? List<String> ?: listOf()
                        orgsToFetch.addAll(path.filter { it.isNotEmpty() })

                        if (orgsToFetch.isEmpty()) {
                            newsList.clear()
                            newsAdapter.notifyDataSetChanged()
                            return@addOnSuccessListener
                        }

                        db.collection("news")
                            .whereIn("organizationId", orgsToFetch)
                            .orderBy("createdAt", Query.Direction.DESCENDING)
                            .get()
                            .addOnSuccessListener { documents ->
                                val tempNewsList = mutableListOf<NewsPost>()
                                val userIds = mutableSetOf<String>()
                                
                                for (document in documents) {
                                    val userId = document.getString("userId") ?: ""
                                    if (userId.isNotEmpty()) userIds.add(userId)
                                }

                                // Fetch all authors at once
                                val userTasks = userIds.map { id ->
                                    db.collection("users").document(id).get()
                                }

                                Tasks.whenAllComplete(userTasks).addOnCompleteListener {
                                    val authorMap = mutableMapOf<String, String>()
                                    for (task in userTasks) {
                                        if (task.isSuccessful) {
                                            val doc = task.result
                                            val first = doc.getString("firstName") ?: ""
                                            val last = doc.getString("lastName") ?: ""
                                            authorMap[doc.id] = "$first $last".trim()
                                        }
                                    }

                                    newsList.clear()
                                    val sdf = SimpleDateFormat("MMM dd, yyyy • h:mm a", Locale.getDefault())
                                    
                                    for (document in documents) {
                                        val id = document.id
                                        val title = document.getString("title") ?: ""
                                        val content = document.getString("content") ?: ""
                                        val authorId = document.getString("userId") ?: ""
                                        val authorName = authorMap[authorId] ?: "Unknown Author"
                                        
                                        val timestamp = document.getTimestamp("createdAt")
                                        val formattedDate = if (timestamp != null) {
                                            sdf.format(timestamp.toDate())
                                        } else {
                                            "Just now"
                                        }

                                        newsList.add(NewsPost(id, title, content, authorName, formattedDate))
                                    }
                                    newsAdapter.notifyDataSetChanged()
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Firestore Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
            }
    }
}