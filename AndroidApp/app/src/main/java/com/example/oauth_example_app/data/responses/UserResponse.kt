package com.example.oauth_example_app.data.responses

data class UserResponse(
    val created_at: String,
    val email: String,
    val first_name: String?,
    val id: Int,
    val is_active: Boolean,
    val last_name: String?,
    val phone_number: Any?,
    val profile_picture: Any?,
)

data class UserUpdateRequest(
    val first_name: String?,
    val last_name: String?
)