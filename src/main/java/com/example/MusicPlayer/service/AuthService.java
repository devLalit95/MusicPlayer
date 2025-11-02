package com.example.MusicPlayer.service;

import com.example.MusicPlayer.dto.JwtResponse;
import com.example.MusicPlayer.dto.LoginRequest;
import com.example.MusicPlayer.dto.RegisterRequest;
import com.example.MusicPlayer.model.User;
import com.example.MusicPlayer.repository.UserRepository;
import com.example.MusicPlayer.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public JwtResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(User.Role.USER);

        User savedUser = userRepository.save(user);

        String jwt = jwtService.generateToken(savedUser.getUsername());
        return new JwtResponse(jwt, savedUser.getId(), savedUser.getUsername(),
                savedUser.getEmail(), savedUser.getRole().name());
    }

    public JwtResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginRequest.getUsername()));

            String jwt = jwtService.generateToken(user.getUsername());
            return new JwtResponse(jwt, user.getId(), user.getUsername(),
                    user.getEmail(), user.getRole().name());
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}