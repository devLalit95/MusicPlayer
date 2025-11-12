package com.example.MusicPlayer.controller;

import com.example.MusicPlayer.dto.SongRequest;
import com.example.MusicPlayer.model.Song;
import com.example.MusicPlayer.repository.SongRepository;
import com.example.MusicPlayer.security.CloudinaryService;
import com.example.MusicPlayer.service.AuthService;
import com.example.MusicPlayer.service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    private final SongService songService;
    private final CloudinaryService cloudinaryService;
    private final SongRepository songRepository;
    public AdminController(AuthService authService, SongService songService, CloudinaryService cloudinaryService, SongRepository songRepository) {
        this.songService = songService;
        this.cloudinaryService = cloudinaryService;
        this.songRepository = songRepository;
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadSong(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("artist") String artist,
            @RequestParam(value = "album", required = false) String album,
            @RequestParam(value = "durationSeconds", required = false) Integer durationSeconds
    ) throws IOException {

        String fileUrl = cloudinaryService.uploadSong(file);

        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setAlbum(album);
        song.setFileUrl(fileUrl);
        song.setDurationSeconds(durationSeconds);
        song.setCreatedAt(LocalDateTime.now());

        songRepository.save(song);

        return ResponseEntity.ok(Map.of(
                "message", "Song uploaded successfully!",
                "url", fileUrl
        ));
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
