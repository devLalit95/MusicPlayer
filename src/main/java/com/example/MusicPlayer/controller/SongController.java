package com.example.MusicPlayer.controller;

import com.example.MusicPlayer.model.Song;
import com.example.MusicPlayer.service.SongServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SongController {

    private final SongServiceInterface songService;

    public SongController(SongServiceInterface songService) {
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

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeSong(@PathVariable("id") Long id) {
        songService.likeSong(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<Void> dislikeSong(@PathVariable("id") Long id) {
        songService.dislikeSong(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/liked")
    public ResponseEntity<List<Song>> getUserLikedSongs() {
        List<Song> songs = songService.getUserLikedSongs();
        return ResponseEntity.ok(songs);
    }
}