package com.northcoders.jv_events_platform_frontend.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.northcoders.jv_events_platform_frontend.R
import com.northcoders.jv_events_platform_frontend.viewmodel.EventViewModel
import com.northcoders.jv_events_platform_frontend.viewmodel.UserViewModel

class MyEventsFragment : Fragment() {
    private val eventViewModel: EventViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_events, container, false)
        recyclerView = view.findViewById(R.id.my_events_recycler_view)
        
        setupRecyclerView()
        observeViewModel()

        return view
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(
            onAddToCalendar = { event -> 
                event.id?.let { eventViewModel.addToGoogleCalendar(it) }
            },
            onUpdate = { event ->
                // Handle update if needed
            },
            onDelete = { event ->
                event.id?.let { eventViewModel.deleteEvent(it) }
            },
            isStaff = { userViewModel.isStaff },
            isLoggedIn = { userViewModel.isLoggedIn }
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        eventViewModel.myEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        eventViewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Load user's events when fragment is created
        eventViewModel.loadMyEvents()
    }

    override fun onResume() {
        super.onResume()
        // Refresh the list when returning to this fragment
        eventViewModel.loadMyEvents()
    }
}