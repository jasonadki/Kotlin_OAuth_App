package com.example.oauth_example_app.data.responses.Strike

data class StrikePaymentExecutionResponse(
    val amount: Amount,
    val completed: String,
    val delivered: String,
    val paymentId: String,
    val result: String,
    val state: String,
    val totalAmount: TotalAmount
)