package com.example.oauth_example_app.data.responses.Github

data class Plan(
    val collaborators: Int,
    val name: String,
    val private_repos: Int,
    val space: Int
)