package com.example.oauth_example_app.data.responses.Marketplace

data class DriverResponse(
    val created_at: String,
    val email: String,
    val first_name: String,
    val gender: Int,
    val id: Int,
    val is_active: Boolean,
    val last_name: String,
    val phone_number: Any,
    val profile_picture: Any
)