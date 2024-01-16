package com.example.oauth_example_app.data.responses.Marketplace

data class TripDetails(
    val driver: Int,
    val end_date: String,
    val id: Int,
    val policies: List<Int>,
    val start_date: String,
    val vehicle: Int
)