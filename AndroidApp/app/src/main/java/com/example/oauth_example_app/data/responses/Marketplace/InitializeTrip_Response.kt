package com.example.oauth_example_app.data.responses.Marketplace

data class InitializeTrip_Response(
    val quote_id: Int,
    val success: String,
    val trip_id: Int,
    val lnAddress: String,
    val start_date: String
)