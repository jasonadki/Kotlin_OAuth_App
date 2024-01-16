package com.example.oauth_example_app.data.responses.Stackoverflow

data class StackoverflowAccessCodeResponse(
    val access_token: String,
    val scope: String?,
    val token_type: String?
)