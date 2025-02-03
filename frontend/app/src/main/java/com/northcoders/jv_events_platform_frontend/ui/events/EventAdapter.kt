package com.northcoders.jv_events_platform_frontend.ui.events

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.northcoders.jv_events_platform_frontend.R
import com.northcoders.jv_events_platform_frontend.model.Event
import com.northcoders.jv_events_platform_frontend.ui.auth.LoginActivity  // Add this import
import java.time.format.DateTimeFormatter

class EventAdapter(
    private val onAddToCalendar: (Event) -> Unit,
    private val onUpdate: (Event) -> Unit,
    private val onDelete: (Event) -> Unit,
    private val isStaff: () -> Boolean,
    private val isLoggedIn: () -> Boolean
) : ListAdapter<Event, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.event_title)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.event_description)
        private val dateTimeTextView: TextView = itemView.findViewById(R.id.event_datetime)
        private val locationTextView: TextView = itemView.findViewById(R.id.event_location)
        private val addToCalendarButton: Button = itemView.findViewById(R.id.add_to_calendar_button)
        private val updateButton: Button = itemView.findViewById(R.id.update_button)
        private val deleteButton: Button = itemView.findViewById(R.id.delete_button)

        private val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")

        fun bind(event: Event) {
            titleTextView.text = event.title
            descriptionTextView.text = event.description
            dateTimeTextView.text = "${event.startDateTime.format(dateTimeFormatter)} - ${event.endDateTime.format(dateTimeFormatter)}"
            locationTextView.text = event.location

            // Show/hide buttons based on user role and login status
            addToCalendarButton.visibility = if (isLoggedIn()) View.VISIBLE else View.GONE
            updateButton.visibility = if (isStaff()) View.VISIBLE else View.GONE
            deleteButton.visibility = if (isStaff()) View.VISIBLE else View.GONE

            addToCalendarButton.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    if (isLoggedIn()) {
                        onAddToCalendar(event)
                    } else {
                        // Show login prompt
                        Toast.makeText(context, "Please log in to add events to calendar", Toast.LENGTH_LONG).show()
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    private class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}