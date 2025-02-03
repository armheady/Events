package com.northcoders.jv_events_platform_frontend.model

import java.time.LocalDateTime

data class Event(
    val id: Long = -1,  // Make id optional with default value
    val title: String,
    val description: String,
    val location: String,
    val startDateTime: String,
    val endDateTime: String,
    val googleCalendarEventId: String? = null
)