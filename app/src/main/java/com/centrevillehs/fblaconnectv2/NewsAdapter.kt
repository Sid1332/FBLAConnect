package com.centrevillehs.fblaconnectv2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter(private val newsList: List<NewsPost>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtNewsTitle)
        val author: TextView = view.findViewById(R.id.txtNewsAuthor)
        val snippet: TextView = view.findViewById(R.id.txtNewsContentSnippet)
        val date: TextView = view.findViewById(R.id.txtNewsDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.title.text = news.title
        holder.author.text = "Posted by: ${news.authorName}"
        holder.snippet.text = news.content
        holder.date.text = news.formattedDate
    }

    override fun getItemCount() = newsList.size
}