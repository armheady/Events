package com.northcoders.jv_events_platform_frontend.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.northcoders.jv_events_platform_frontend.api.ApiClient
import com.northcoders.jv_events_platform_frontend.model.User
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    val isLoggedIn: Boolean
        get() = _user.value != null

    val isStaff: Boolean
        get() = _user.value?.role == "staff"

    fun login() {
        viewModelScope.launch {
            try {
                val response = ApiClient.eventsApiService.login()
                if (!response.isSuccessful) {
                    _message.value = "Login failed"
                }
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    fun setUser(user: User) {
        _user.value = user
    }

    fun logout() {
        _user.value = null
    }
}