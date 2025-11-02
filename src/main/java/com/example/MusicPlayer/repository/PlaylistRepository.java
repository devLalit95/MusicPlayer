package com.example.MusicPlayer.repository;


import com.example.MusicPlayer.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> findByUserId(Long userId);

    Optional<Playlist> findByIdAndUserId(Long id, Long userId);

    List<Playlist> findByNameContainingIgnoreCaseAndUserId(String name, Long userId);
}