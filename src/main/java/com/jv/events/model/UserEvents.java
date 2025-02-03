package com.jv.events.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime; 

@Entity
@Data
@Table(name = "user_events")
public class UserEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Many user_event records can be associated with one user
    //Foreign key in user_events table referencing primary key(id) in user table
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    private LocalDateTime registrationDate;

}
