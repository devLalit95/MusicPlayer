package com.example.MusicPlayer.service;

import com.example.MusicPlayer.dto.UserProfileResponse;
import com.example.MusicPlayer.dto.UserProfileUpdateRequest;
import com.example.MusicPlayer.model.User;

import java.util.List;

public interface UserServiceInterface {

    List<User> getAllUsers();

    void deleteUser(Long userId);

    UserProfileResponse getCurrentUserProfile();

    UserProfileResponse updateCurrentUserProfile(UserProfileUpdateRequest profileUpdateRequest);
}
