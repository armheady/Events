package com.jv.events.repository;

import com.jv.events.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //using an optional prevents returning null if user doesn't exist avoids NullPointerException
    Optional<User> findByEmail(String email);

}
