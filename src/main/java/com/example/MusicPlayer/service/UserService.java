package com.example.MusicPlayer.service;

import com.example.MusicPlayer.model.User;
import com.example.MusicPlayer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ADMIN ONLY
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ADMIN ONLY
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}
