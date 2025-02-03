package com.jv.events.controller;

import com.jv.events.model.Event;
import com.jv.events.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEventsOrderedByDateDesc();
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/myevents")
    public List<Event> getMyEvents(@AuthenticationPrincipal OAuth2User principal) {
        String userEmail = principal.getAttribute("email");
        return eventService.getMyRegisteredForEvents(userEmail);
    }

    // @GetMapping("/search")
    // public List<Event> searchEvents(@RequestParam(required = false) String
    // title){
    // if(title != null && !title.isEmpty()) return
    // eventService.findEventByTitle(title);
    // return getAllEvents();
    // }

    // need to integrate GoogleCal API/ OAUTh
    // protected endpoints requiring authentication (login)
    // registerForEvent

    // @PostMapping("/{id}/register")
    // public ResponseEntity<String> registerForEvent(@PathVariable Long id,
    // @AuthenticationPrincipal OAuth2User principal) {
    // String userEmail = principal.getAttribute("email");
    // eventService.registerForEvent(id, userEmail);
    // return ResponseEntity.ok("Registration successful");
    // }

    // @DeleteMapping("/{id}/register")
    // public ResponseEntity<String> unregisterForEvent(@PathVariable Long id,
    // @AuthenticationPrincipal OAuth2User principal) {
    // String userEmail = principal.getAttribute("email");
    // eventService.unregisterForEvent(id, userEmail);
    // return ResponseEntity.ok("Unregistered from event");
    // }

    @GetMapping("/{id}/calendar")
    public ResponseEntity<String> addToGoogleCalendar(@PathVariable Long id,
            @AuthenticationPrincipal OAuth2User principal) throws IOException, GeneralSecurityException {
        String userEmail = principal.getAttribute("email");
        eventService.addEventToGoogleCalendar(id, userEmail);
        return ResponseEntity.ok("Event added to Google Calendar");
    }

    @DeleteMapping("/{id}/calendar")
    public ResponseEntity<String> removeFromGoogleCalendar(@PathVariable Long id,
            @AuthenticationPrincipal OAuth2User principal) throws IOException, GeneralSecurityException {
        String userEmail = principal.getAttribute("email");
        eventService.removeEventFromGoogleCalendar(id, userEmail);
        return ResponseEntity.ok("Event removed from Google Calendar");
    }

    // Staff only endpoint role=staff
    // createEvent
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        event.setId(null);
        return eventService.createEvent(event);
    }

    // updateEvent
    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    // deleteEvent
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

    @GetMapping("/login")
    public void login(
            @RequestParam("device_id") String deviceId,
            @RequestParam("device_name") String deviceName,
            HttpServletResponse response) throws IOException {
        String redirectUrl = "/oauth2/authorization/google" +
                "?device_id=" + deviceId +
                "&device_name=" + URLEncoder.encode(deviceName, StandardCharsets.UTF_8.toString());
        response.sendRedirect(redirectUrl);
    }

}
