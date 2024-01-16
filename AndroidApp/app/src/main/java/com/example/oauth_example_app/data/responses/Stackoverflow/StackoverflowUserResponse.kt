package com.example.oauth_example_app.data.responses.Stackoverflow

data class StackoverflowUserResponse(
    val has_more: Boolean,
    val items: List<Item>,
    val quota_max: Int,
    val quota_remaining: Int
)