package com.centrevillehs.fblaconnectv2

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class CompetitionsFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var competitionsRecyclerView: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var competitionAdapter: CompetitionAdapter
    private var allCompetitions = listOf<Competition>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_competitions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()

        competitionsRecyclerView = view.findViewById(R.id.competitionsRecyclerView)
        searchBar = view.findViewById(R.id.searchBar)

        competitionAdapter = CompetitionAdapter(listOf()) { competition ->
            showCompetitionDetails(competition)
        }

        competitionsRecyclerView.adapter = competitionAdapter
        competitionsRecyclerView.layoutManager = LinearLayoutManager(context)

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCompetitions(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        fetchCompetitions()
    }

    private fun fetchCompetitions() {
        db.collection("competitions")
            .get()
            .addOnSuccessListener { result ->
                val competitions = result.map { document ->
                    document.toObject(Competition::class.java)
                }
                allCompetitions = competitions
                competitionAdapter.updateCompetitions(competitions)
            }
    }

    private fun filterCompetitions(query: String) {
        val filteredList = allCompetitions.filter {
            it.name.contains(query, ignoreCase = true)
        }
        competitionAdapter.updateCompetitions(filteredList)
    }

    private fun showCompetitionDetails(competition: Competition) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_competition_details, null)
        val competitionName = dialogView.findViewById<TextView>(R.id.competitionName)
        val competitionType = dialogView.findViewById<TextView>(R.id.competitionType)
        val competitionLevel = dialogView.findViewById<TextView>(R.id.competitionLevel)
        val competitionDescription = dialogView.findViewById<TextView>(R.id.competitionDescription)
        val detailsButton = dialogView.findViewById<Button>(R.id.detailsButton)

        competitionName.text = competition.name
        competitionType.text = competition.type
        competitionLevel.text = competition.level
        competitionDescription.text = competition.popup_description

        detailsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(competition.pdf_link))
            startActivity(intent)
        }

        AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
            .show()
    }
}