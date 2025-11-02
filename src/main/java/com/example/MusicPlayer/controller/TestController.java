package com.example.MusicPlayer.controller;

import com.example.MusicPlayer.model.Song;
import com.example.MusicPlayer.model.User;
import com.example.MusicPlayer.repository.SongRepository;
import com.example.MusicPlayer.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public TestController(UserRepository userRepository, SongRepository songRepository) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
    }

    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "This is a public endpoint",
                "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }

    @GetMapping("/protected")
    public ResponseEntity<Map<String, Object>> protectedEndpoint() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(Map.of(
                "message", "This is a protected endpoint",
                "user", username,
                "timestamp", java.time.LocalDateTime.now().toString(),
                "status", "authenticated"
        ));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/songs/count")
    public ResponseEntity<Map<String, Object>> getSongCount() {
        long count = songRepository.count();
        return ResponseEntity.ok(Map.of(
                "totalSongs", count,
                "message", "Total songs in database"
        ));
    }

    @GetMapping("/songs/random")
    public ResponseEntity<List<Song>> getRandomSongs() {
        List<Song> songs = songRepository.findAll();
        // Return first 5 songs as random sample
        List<Song> randomSongs = songs.stream().limit(5).toList();
        return ResponseEntity.ok(randomSongs);
    }

    @PostMapping("/create-song")
    public ResponseEntity<Map<String, Object>> createSampleSong() {
        Song song = new Song();
        song.setTitle("New Sample Song " + System.currentTimeMillis());
        song.setArtist("Sample Artist");
        song.setAlbum("Sample Album");
        song.setFileUrl("https://example.com/songs/sample_song.mp3");
        song.setDurationSeconds(180);

        Song savedSong = songRepository.save(song);

        return ResponseEntity.ok(Map.of(
                "message", "Sample song created successfully",
                "song", savedSong,
                "id", savedSong.getId()
        ));
    }

    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("role", user.getRole());
        userInfo.put("createdAt", user.getCreatedAt());
        userInfo.put("playlistCount", user.getPlaylists() != null ? user.getPlaylists().size() : 0);

        return ResponseEntity.ok(userInfo);
    }
}