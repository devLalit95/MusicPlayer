package com.example.MusicPlayer.controller;

import com.example.MusicPlayer.dto.SongRequest;
import com.example.MusicPlayer.model.Song;
import com.example.MusicPlayer.service.AuthService;
import com.example.MusicPlayer.service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    private final AuthService authService;
    private final SongService songService;

    public AdminController(AuthService authService, SongService songService) {
        this.authService = authService;
        this.songService = songService;
    }

    // ✅ ADMIN — CRUD for Songs

    @GetMapping("/songs")
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songService.getAllSongs();
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        Song song = songService.getSongById(id);
        return ResponseEntity.ok(song);
    }

    @PostMapping("/songs")
    public ResponseEntity<Song> createSong(@Valid @RequestBody SongRequest songRequest) {
        Song song = songService.createSong(songRequest);
        return ResponseEntity.ok(song);
    }

    @PutMapping("/songs/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @Valid @RequestBody SongRequest songRequest) {
        Song song = songService.updateSong(id, songRequest);
        return ResponseEntity.ok(song);
    }

    @DeleteMapping("/songs/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }


}
