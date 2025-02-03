package com.jv.events.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.jv.events.model.Event;
import com.jv.events.model.User;
import com.jv.events.model.UserEvents;
import com.jv.events.repository.EventRepository;
import com.jv.events.repository.UserEventsRepository;
import com.jv.events.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.EventDateTime;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEventsRepository userEventsRepository;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public List<Event> getAllEvents() {
        // returns empty list if no events
        return eventRepository.findAll();
    }

    public List<Event> getAllEventsOrderedByDateDesc() {
        return eventRepository.findAllByOrderByStartDateTimeDesc();
    }

    public List<Event> getMyRegisteredForEvents(String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (!userOptional.isPresent())
            throw new RuntimeException("User not found");
        User user = userOptional.get();

        List<UserEvents> userEvents = userEventsRepository.findByUser(user);
        List<Event> events = new ArrayList<>();
        for (UserEvents userEvent : userEvents) {
            events.add(userEvent.getEvent());
        }
        return events;
    }

    public Event getEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (!event.isPresent())
            throw new RuntimeException("Event not found");
        return event.get();
    }

    public List<Event> findEventByTitle(String title) {
        return eventRepository.findByTitleIgnoreCase(title);
    }

    public List<Event> findEventByStartDateTime(LocalDateTime start) {
        return eventRepository.findByStartDateTime(start);
    }

    public Event createEvent(Event event) {
        // Set id to null for new events
        event.setId(null);
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }

    public Event updateEvent(Long id, Event eventInformation) {
        Event event = getEventById(id);

        event.setTitle(eventInformation.getTitle());
        event.setDescription(eventInformation.getDescription());
        event.setStartDateTime(eventInformation.getStartDateTime());
        event.setEndDateTime(eventInformation.getEndDateTime());
        event.setLocation(eventInformation.getLocation());
        return eventRepository.save(event);
    }

    public void registerForEvent(Long eventId, String userEmail) {
        Event event = getEventById(eventId);
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (!userOptional.isPresent())
            throw new RuntimeException("User not found");
        User user = userOptional.get();

        // Check if user is already registered
        if (!userEventsRepository.existsByUserAndEvent(user, event)) {
            UserEvents userEvent = new UserEvents();
            userEvent.setUser(user);
            userEvent.setEvent(event);
            userEvent.setRegistrationDate(LocalDateTime.now());
            userEventsRepository.save(userEvent);
        }
    }

    public void unregisterForEvent(Long eventId, String userEmail) {
        Event event = getEventById(eventId);
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (!userOptional.isPresent())
            throw new RuntimeException("User not found");
        User user = userOptional.get();

        UserEvents userEvent = userEventsRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        userEventsRepository.delete(userEvent);
    }

    public void addEventToGoogleCalendar(Long eventId, String userEmail) throws IOException, GeneralSecurityException {
        Event event = getEventById(eventId);
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (!userOptional.isPresent())
            throw new RuntimeException("User not found");
        User user = userOptional.get();

        Calendar service = getCalendarService(user.getGoogleAccessToken());

        com.google.api.services.calendar.model.Event googleEvent = new com.google.api.services.calendar.model.Event()
                .setSummary(event.getTitle())
                .setLocation(event.getLocation())
                .setDescription(event.getDescription());

        // Fix date format
        DateTime startDateTime = new DateTime(event.getStartDateTime().toString() + ":00Z");
        EventDateTime start = new EventDateTime().setDateTime(startDateTime);
        googleEvent.setStart(start);

        DateTime endDateTime = new DateTime(event.getEndDateTime().toString() + ":00Z");
        EventDateTime end = new EventDateTime().setDateTime(endDateTime);
        googleEvent.setEnd(end);

        // Only register user if calendar operation succeeds
        String calendarEventId = service.events().insert("primary", googleEvent).execute().getId();
        event.setGoogleCalendarEventId(calendarEventId);
        eventRepository.save(event);
        // Register user after successful calendar addition
        registerForEvent(eventId, userEmail);
    }

    public void removeEventFromGoogleCalendar(Long eventId, String userEmail)
            throws IOException, GeneralSecurityException {
        Event event = getEventById(eventId);
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (!userOptional.isPresent())
            throw new RuntimeException("User not found");
        User user = userOptional.get();

        if (event.getGoogleCalendarEventId() != null) {
            Calendar service = getCalendarService(user.getGoogleAccessToken());
            // Only unregister user if calendar operation succeeds
            service.events().delete("primary", event.getGoogleCalendarEventId()).execute();
            event.setGoogleCalendarEventId(null);
            eventRepository.save(event);
            // Unregister user after successful calendar removal
            unregisterForEvent(eventId, userEmail);
        }
    }

    private Calendar getCalendarService(String accessToken) throws GeneralSecurityException, IOException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .setApplicationName("JV Events Backend")
                .build();
    }
}
