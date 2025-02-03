package com.jv.events.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDateTime startDateTime;
    // separate date and time fields?
    @Column(nullable = false)
    private LocalDateTime endDateTime;
    // links local event to google calendar event so can update and delete events
    private String googleCalendarEventId;
    private String location;

}
