package com.northcoders.jv_events_platform_frontend.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.northcoders.jv_events_platform_frontend.R
import com.northcoders.jv_events_platform_frontend.viewmodel.UserViewModel

class LoginFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        loginButton = view.findViewById(R.id.google_login_button)

        setupLoginButton()
        observeViewModel()

        return view
    }

    private fun setupLoginButton() {
        loginButton.setOnClickListener {
            userViewModel.login()
        }
    }

    private fun observeViewModel() {
        userViewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                // Navigate to events list after successful login
                // TODO: Implement navigation
            }
        }
    }
}