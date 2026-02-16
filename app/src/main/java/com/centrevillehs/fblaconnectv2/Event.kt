package com.centrevillehs.fblaconnectv2

data class Event(
    val id: String = "",
    val name: String = "",
    val date: String = "", // MM/DD/YYYY
    val description: String = "",
    val organizationId: String = ""
)