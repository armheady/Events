package com.northcoders.jv_events_platform_frontend.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.northcoders.jv_events_platform_frontend.api.ApiClient
import com.northcoders.jv_events_platform_frontend.model.Event
import kotlinx.coroutines.launch
import android.util.Log

class EventViewModel : ViewModel() {
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> = _navigateBack
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    private val _myEvents = MutableLiveData<List<Event>>()
    val myEvents: LiveData<List<Event>> = _myEvents

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun loadAllEvents() {
        viewModelScope.launch {
            try {
                val response = ApiClient.eventsApiService.getAllEvents()
                if (response.isSuccessful) {
                    _events.value = response.body()
                } else {
                    _message.value = "Failed to load events"
                }
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    fun loadMyEvents() {
        viewModelScope.launch {
            try {
                val response = ApiClient.eventsApiService.getMyEvents()
                if (response.isSuccessful) {
                    _myEvents.value = response.body()
                } else {
                    _message.value = "Failed to load your events"
                }
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    fun createEvent(event: Event) {
        viewModelScope.launch {
            try {
                Log.i("Creating event: ",event.toString())
                val response = ApiClient.eventsApiService.createEvent(event)
                println("Response code: ${response.code()}")
                println("Response headers: ${response.headers()}")
                println("Response body: ${response.body()}")
                println("Error body: ${response.errorBody()?.string()}")
                
                if (response.isSuccessful) {
                    _message.value = "Event created successfully"
                    loadAllEvents()
                    _navigateBack.value = true
                } else {
                    _message.value = "Failed to create event: ${response.code()}"
                }
            } catch (e: Exception) {
                println("Exception type: ${e.javaClass.name}")
                println("Exception message: ${e.message}")
                _message.value = "Failed to create event: ${e.message}"
            }
        }
    }

    fun updateEvent(id: Long, event: Event) {
        viewModelScope.launch {
            try {
                val response = ApiClient.eventsApiService.updateEvent(id, event)
                if (response.isSuccessful) {
                    _message.value = "Event updated successfully"
                    loadAllEvents()
                } else {
                    _message.value = "Failed to update event"
                }
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    fun deleteEvent(id: Long) {
        viewModelScope.launch {
            try {
                val response = ApiClient.eventsApiService.deleteEvent(id)
                if (response.isSuccessful) {
                    _message.value = "Event deleted successfully"
                    loadAllEvents()
                } else {
                    _message.value = "Failed to delete event"
                }
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    fun addToGoogleCalendar(eventId: Long) {
        viewModelScope.launch {
            try {
                val response = ApiClient.eventsApiService.addToGoogleCalendar(eventId)
                if (response.isSuccessful) {
                    _message.value = "Event added to calendar successfully"
                } else {
                    _message.value = "Failed to add event to calendar"
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }
}