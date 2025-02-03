package com.northcoders.jv_events_platform_frontend.ui.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.northcoders.jv_events_platform_frontend.R
import com.northcoders.jv_events_platform_frontend.model.Event
import com.northcoders.jv_events_platform_frontend.viewmodel.EventViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateEventFragment : Fragment() {
    private val viewModel: EventViewModel by viewModels()
    private var startDateTime: LocalDateTime? = null
    private var endDateTime: LocalDateTime? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        view.findViewById<Button>(R.id.start_date_button).setOnClickListener {
            showDateTimePicker { dateTime ->
                startDateTime = dateTime
                updateDateTimeButton(it as Button, dateTime)
            }
        }

        view.findViewById<Button>(R.id.end_date_button).setOnClickListener {
            showDateTimePicker { dateTime ->
                endDateTime = dateTime
                updateDateTimeButton(it as Button, dateTime)
            }
        }

        view.findViewById<Button>(R.id.create_event_button).setOnClickListener {
            createEvent()
        }

        // Add observer for messages
        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun showDateTimePicker(onDateTimeSelected: (LocalDateTime) -> Unit) {
        val now = LocalDateTime.now()
        
        DatePickerDialog(requireContext(), { _, year, month, day ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                onDateTimeSelected(LocalDateTime.of(year, month + 1, day, hour, minute))
            }, now.hour, now.minute, true).show()
        }, now.year, now.monthValue - 1, now.dayOfMonth).show()
    }

    private fun updateDateTimeButton(button: Button, dateTime: LocalDateTime) {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
        button.text = dateTime.format(formatter)
    }

    private fun createEvent() {
        val title = view?.findViewById<EditText>(R.id.title_input)?.text.toString()
        val description = view?.findViewById<EditText>(R.id.description_input)?.text.toString()
        val location = view?.findViewById<EditText>(R.id.location_input)?.text.toString()

        if (title.isBlank() || description.isBlank() || location.isBlank() || 
            startDateTime == null || endDateTime == null) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val event = Event(
            title = title,
            description = description,
            location = location,
            startDateTime = startDateTime!!.format(formatter),
            endDateTime = endDateTime!!.format(formatter),
            googleCalendarEventId = null  // Add this line
        )

        viewModel.createEvent(event)
        parentFragmentManager.popBackStack()
    }
}