package com.jv.events.repository;
import com.jv.events.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long> {
    //Spring DataJpa can derive SQL db queries from methods names
    //As long as field from entity are referred to using correct naming convention
    //The first part — such as find — is the introducer, and the rest — such as ByName — is the criteria.
    List<Event> findAll();
    List<Event> findByTitleIgnoreCase(String title);
    List<Event> findByStartDateTime(LocalDateTime start);
    Optional<Event> findById(Long id);
    List<Event> findAllByOrderByStartDateTimeDesc();
//     void deleteById(Long id);
}
