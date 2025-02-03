package com.northcoders.jv_events_platform_frontend.api

import com.northcoders.jv_events_platform_frontend.model.Event
import retrofit2.Response
import retrofit2.http.*

interface EventsApiService {
    @GET("/api/events")
    suspend fun getAllEvents(): Response<List<Event>>

    @GET("/api/events/{id}")
    suspend fun getEvent(@Path("id") id: Long): Response<Event>

    @GET("/api/events/myevents")
    suspend fun getMyEvents(): Response<List<Event>>

    @GET("/api/events/{id}/calendar")
    suspend fun addToGoogleCalendar(@Path("id") id: Long): Response<String>

    @DELETE("/api/events/{id}/calendar")
    suspend fun removeFromGoogleCalendar(@Path("id") id: Long): Response<String>

    @POST("/api/events")
    suspend fun createEvent(@Body event: Event): Response<Event>

    @PUT("/api/events/{id}")
    suspend fun updateEvent(@Path("id") id: Long, @Body event: Event): Response<Event>

    @DELETE("/api/events/{id}")
    suspend fun deleteEvent(@Path("id") id: Long): Response<Unit>

    @GET("/api/events/login")
    suspend fun login(): Response<Unit>
}