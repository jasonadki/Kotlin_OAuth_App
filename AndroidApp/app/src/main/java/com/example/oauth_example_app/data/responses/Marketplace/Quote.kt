package com.example.oauth_example_app.data.responses.Marketplace

data class Quote(
    val id: Int,
    val latitude: String,
    val longitude: String,
    val rate_offering: RateOffering,
    val start_date: String,
    val driver: DriverResponse,
    val vehicle: VehicleResponse

)