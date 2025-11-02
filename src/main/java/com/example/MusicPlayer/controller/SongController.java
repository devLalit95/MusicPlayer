package com.example.MusicPlayer.controller;


import com.example.MusicPlayer.dto.SongRequest;
import com.example.MusicPlayer.model.Song;
import com.example.MusicPlayer.service.SongService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songService.getAllSongs();
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        Song song = songService.getSongById(id);
        return ResponseEntity.ok(song);
    }

    @PostMapping
    public ResponseEntity<Song> createSong(@Valid @RequestBody SongRequest songRequest) {
        Song song = songService.createSong(songRequest);
        return ResponseEntity.ok(song);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @Valid @RequestBody SongRequest songRequest) {
        Song song = songService.updateSong(id, songRequest);
        return ResponseEntity.ok(song);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Song>> searchSongsByTitle(@RequestParam String title) {
        List<Song> songs = songService.searchSongsByTitle(title);
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/search/artist")
    public ResponseEntity<List<Song>> searchSongsByArtist(@RequestParam String artist) {
        List<Song> songs = songService.searchSongsByArtist(artist);
        return ResponseEntity.ok(songs);
    }
}