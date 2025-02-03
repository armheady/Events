package com.northcoders.jv_events_platform_frontend

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.appcompat.widget.Toolbar
import com.northcoders.jv_events_platform_frontend.ui.events.EventListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.northcoders.jv_events_platform_frontend.ui.events.MyEventsFragment

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = "Events"

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EventListFragment())
                .commit()
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_events -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, EventListFragment())
                        .commit()
                    true
                }
                R.id.navigation_my_events -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,   MyEventsFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

        // Set default selection
        if (savedInstanceState == null) {
            bottomNavigation.selectedItemId = R.id.navigation_events
        }
    }
}