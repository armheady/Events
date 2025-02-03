package com.northcoders.jv_events_platform_frontend.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.northcoders.jv_events_platform_frontend.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "Login"

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }
    }
}