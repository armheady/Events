package com.jv.events.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    //Above annotations store in enum as string in database.
    //If not specified stores enum as number based on its ordinal positions
    private Role role;
    //to store access token from OAuth in db can then be used for calendar operations
    private String googleAccessToken;

    @OneToMany(mappedBy = "user")
    //user_events table contains foreign key references the users table
    private Set<UserEvents> userEvents;
}
