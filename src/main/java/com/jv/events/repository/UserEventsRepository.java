package com.jv.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jv.events.model.UserEvents;
import com.jv.events.model.User;
import com.jv.events.model.Event;
import java.util.Optional;
import java.util.List;

public interface UserEventsRepository extends JpaRepository<UserEvents, Long> {
    List<UserEvents> findByUser(User user);
    boolean existsByUserAndEvent(User user, Event event);
    Optional<UserEvents> findByUserAndEvent(User user, Event event);
}