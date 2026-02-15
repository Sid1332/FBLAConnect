package com.centrevillehs.fblaconnectv2

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class ResourcesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resources)

        findViewById<CardView>(R.id.resource_crosswalk).setOnClickListener {
            openUrl("https://connect.fbla.org/headquarters/files/High%20School%20Competitive%20Events%20Resources/25-26-HS-CE-NACE-Crosswalk.pdf")
        }

        findViewById<CardView>(R.id.resource_guidelines).setOnClickListener {
            openUrl("https://connect.fbla.org/headquarters/files/High%20School%20Competitive%20Events%20Resources/25-26-High-School-Guidelines-All-in-One.pdf")
        }

        findViewById<CardView>(R.id.resource_choose_event).setOnClickListener {
            openUrl("https://storage.googleapis.com/articulate-courses/BAA%20Contributor/Choose%20Your%20Event%20(FBLA%20High%20School)/story.html")
        }

        findViewById<CardView>(R.id.resource_event_descriptions).setOnClickListener {
            openUrl("https://www.fbla.org/high-school/competitive-events/?_event_search=mob#:~:text=Competitive%20Event%20Descriptions,High%20School")
        }

        findViewById<CardView>(R.id.resource_operations_manual).setOnClickListener {
            openUrl("https://connect.fbla.org/headquarters/files/25-26-National-Competitive-Events-Operations-Manual.pdf")
        }

        findViewById<CardView>(R.id.resource_event_topics).setOnClickListener {
            openUrl("https://greektrack-fbla-public.s3.amazonaws.com/files/1/High%20School%20Competitive%20Events%20Resources/25-26-High-School-Topics.pdf?X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAWVAJYMX2IFDGHQE3%2F20260215%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20260215T154115Z&X-Amz-SignedHeaders=host&X-Amz-Expires=300&X-Amz-Signature=0bf8e3c2fae094df4b7be54dc3714b32453ea5003fea3b8ed66eedc427374631")
        }

        findViewById<CardView>(R.id.resource_event_changes).setOnClickListener {
            openUrl("https://connect.fbla.org/headquarters/files/High%20School%20Competitive%20Events%20Resources/25-26-High-School-CE-Changes.pdf")
        }

        findViewById<CardView>(R.id.resource_mba_research).setOnClickListener {
            openUrl("https://www.mbaresearch.org/local-educators/student-organizations/fbla/")
        }

        findViewById<CardView>(R.id.resource_production_test_guide).setOnClickListener {
            openUrl("https://connect.fbla.org/headquarters/files/FBLA-Production-Test-Reference-Guide.pdf")
        }

        findViewById<CardView>(R.id.resource_preparation).setOnClickListener {
            openUrl("https://connect.fbla.org/login.php?action=viewFolder&folder=High%20School%20CE%20Prep%20Resources/&folderid=805")
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}