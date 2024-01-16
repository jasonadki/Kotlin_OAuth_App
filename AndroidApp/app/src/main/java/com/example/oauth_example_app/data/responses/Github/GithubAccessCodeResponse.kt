package com.example.oauth_example_app.data.responses.Github

data class GithubAccessCodeResponse(
    val access_token: String,
    val scope: String?,
    val token_type: String?
)