package com.northcoders.jv_events_platform_frontend.model

data class User(
    val email: String,
    val name: String,
    val role: String,
    val googleAccessToken: String?
)