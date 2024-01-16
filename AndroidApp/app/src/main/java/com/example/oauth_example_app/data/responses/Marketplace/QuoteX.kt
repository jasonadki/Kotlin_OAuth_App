package com.example.oauth_example_app.data.responses.Marketplace

data class QuoteX(
    val driver: Driver,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val rate_offering: RateOfferingX,
    val start_date: String,
    val vehicle: Vehicle
)

