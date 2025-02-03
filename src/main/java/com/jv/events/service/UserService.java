package com.jv.events.service;
import com.jv.events.model.User;
import com.jv.events.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Email already registered");
        }
        return userRepository.save(user);
    }
    public User getUserByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
       if (!user.isPresent()) throw new RuntimeException("User not Found");
       return user.get();
    }

}
