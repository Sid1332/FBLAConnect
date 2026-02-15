package com.centrevillehs.fblaconnectv2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class ResourcesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<CardView>(R.id.resource_crosswalk).setOnClickListener {
            openUrl("https://connect.fbla.org/headquarters/files/High%20School%20Competitive%20Events%20Resources/25-26-HS-CE-NACE-Crosswalk.pdf")
        }

        view.findViewById<CardView>(R.id.resource_guidelines).setOnClickListener {
            openUrl("https://connect.fbla.org/headquarters/files/High%20School%20Competitive%20Events%20Resources/25-26-High-School-Guidelines-All-in-One.pdf")
        }

        view.findViewById<CardView>(R.id.resource_choose_event).setOnClickListener {
            openUrl("https://storage.googleapis.com/articulate-courses/BAA%20Contributor/Choose%20Your%20Event%20(FBLA%20High%20School)/story.html")
        }

        view.findViewById<CardView>(R.id.resource_event_descriptions).setOnClickListener {
            openUrl("https://www.fbla.org/high-school/competitive-events/?_event_search=mob#:~:text=Competitive%20Event%20Descriptions,High%20School")
        }

        view.findViewById<CardView>(R.id.resource_operations_manual).setOnClickListener {
            openUrl("https://connect.fbla.org/headquarters/files/25-26-National-Competitive-Events-Operations-Manual.pdf")
        }

        view.findViewById<CardView>(R.id.resource_event_topics).setOnClickListener {
            openUrl("https://connect.fbla.org/headquarters/files/High%20School%20Competitive%20Events%20Resources/25-26-High-School-Topics.pdf")
        }

        view.findViewById<CardView>(R.id.resource_event_changes).setOnClickListener {
            openUrl("https://connect.fbla.org/headquarters/files/High%20School%20Competitive%20Events%20Resources/25-26-High-School-CE-Changes.pdf")
        }

        view.findViewById<CardView>(R.id.resource_mba_research).setOnClickListener {
            openUrl("https://www.mbaresearch.org/local-educators/student-organizations/fbla/")
        }

        view.findViewById<CardView>(R.id.resource_production_test_guide).setOnClickListener {
            openUrl("https://connect.fbla.org/headquarters/files/FBLA-Production-Test-Reference-Guide.pdf")
        }

        view.findViewById<CardView>(R.id.resource_preparation).setOnClickListener {
            openUrl("https://connect.fbla.org/login.php?action=viewFolder&folder=High%20School%20CE%20Prep%20Resources/&folderid=805")
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}