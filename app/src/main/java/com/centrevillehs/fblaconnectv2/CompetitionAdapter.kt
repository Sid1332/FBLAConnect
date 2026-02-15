package com.centrevillehs.fblaconnectv2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompetitionAdapter(private var competitions: List<Competition>, private val onClick: (Competition) -> Unit) : RecyclerView.Adapter<CompetitionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_competition, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(competitions[position])
    }

    override fun getItemCount() = competitions.size

    fun updateCompetitions(competitions: List<Competition>) {
        this.competitions = competitions
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, val onClick: (Competition) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.competitionName)
        private val levelTextView: TextView = itemView.findViewById(R.id.competitionLevel)
        private val typeTextView: TextView = itemView.findViewById(R.id.competitionType)

        fun bind(competition: Competition) {
            nameTextView.text = competition.name
            levelTextView.text = "Level: ${competition.level}"
            typeTextView.text = "${competition.type} (${competition.team_size})"
            itemView.setOnClickListener { onClick(competition) }
        }
    }
}