package com.example.MusicPlayer.service;

import com.example.MusicPlayer.dto.UserProfileResponse;
import com.example.MusicPlayer.dto.UserProfileUpdateRequest;
import com.example.MusicPlayer.exception.ResourceNotFoundException;
import com.example.MusicPlayer.model.User;
import com.example.MusicPlayer.repository.UserRepository;

import jakarta.validation.constraints.NotNull;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ADMIN ONLY
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ADMIN ONLY
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
    }

    @Override
    public UserProfileResponse getCurrentUserProfile() {
        User user = getCurrentUser();
        return new UserProfileResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name(),
                user.getCreatedAt());
    }

    @Override
    public UserProfileResponse updateCurrentUserProfile(UserProfileUpdateRequest profileUpdateRequest) {
        User user = getCurrentUser();

        if (profileUpdateRequest.getUsername() != null && !profileUpdateRequest.getUsername().isBlank()) {
            user.setUsername(profileUpdateRequest.getUsername());
        }
        if (profileUpdateRequest.getEmail() != null && !profileUpdateRequest.getEmail().isBlank()) {
            user.setEmail(profileUpdateRequest.getEmail());
        }
        if (profileUpdateRequest.getPassword() != null && !profileUpdateRequest.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(profileUpdateRequest.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return new UserProfileResponse(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail(),
                updatedUser.getRole().name(), updatedUser.getCreatedAt());
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
