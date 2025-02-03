package com.northcoders.jv_events_platform_frontend.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.TextView  // Add this import
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.northcoders.jv_events_platform_frontend.R
import com.northcoders.jv_events_platform_frontend.viewmodel.EventViewModel
import com.northcoders.jv_events_platform_frontend.viewmodel.UserViewModel
import com.northcoders.jv_events_platform_frontend.model.Event

class EventListFragment : Fragment() {
    private val eventViewModel: EventViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var createEventButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().setTitle("Events")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_list, container, false)
        
        view.findViewById<FloatingActionButton>(R.id.create_event_button).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateEventFragment())
                .addToBackStack(null)
                .commit()
        }

        recyclerView = view.findViewById(R.id.events_recycler_view)
        
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
                // Navigate to update event screen
            },
            onDelete = { event ->
                event.id?.let { eventViewModel.deleteEvent(it) }  // Fixed method name
            },
            isStaff = { userViewModel.isStaff },
            isLoggedIn = { userViewModel.isLoggedIn }
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        eventViewModel.events.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        eventViewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Load events when fragment is created
        eventViewModel.loadAllEvents()
    }
}