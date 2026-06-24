package com.example.MusicPlayer.controller;

import com.example.MusicPlayer.dto.PlaylistRequest;
import com.example.MusicPlayer.dto.UserProfileResponse;
import com.example.MusicPlayer.dto.UserProfileUpdateRequest;
import com.example.MusicPlayer.model.Playlist;
import com.example.MusicPlayer.model.Song;
import com.example.MusicPlayer.service.PlaylistServiceInterface;
import com.example.MusicPlayer.service.SongServiceInterface;
import com.example.MusicPlayer.service.UserServiceInterface;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final SongServiceInterface songService;
    private final PlaylistServiceInterface playlistService;
    private final UserServiceInterface userService;

    public UserController(SongServiceInterface songService,
            PlaylistServiceInterface playlistService,
            UserServiceInterface userService) {
        this.songService = songService;
        this.playlistService = playlistService;
        this.userService = userService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "App is alive!";
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(@RequestBody UserProfileUpdateRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUserProfile(request));
    }

    @GetMapping("/profile/liked-songs")
    public ResponseEntity<List<Song>> getLikedSongs() {
        return ResponseEntity.ok(songService.getUserLikedSongs());
    }

    @GetMapping("/profile/frequent-songs")
    public ResponseEntity<List<Song>> getFrequentlyPlayedSongs() {
        return ResponseEntity.ok(songService.getUserFrequentlyPlayedSongs());
    }

    @PostMapping("/songs/{songId}/like")
    public ResponseEntity<Void> likeSong(@PathVariable Long songId) {
        songService.likeSong(songId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/songs/{songId}/like")
    public ResponseEntity<Void> dislikeSong(@PathVariable Long songId) {
        songService.dislikeSong(songId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/songs/{songId}/listen")
    public ResponseEntity<Song> playSong(@PathVariable Long songId) {
        return ResponseEntity.ok(songService.recordSongPlay(songId));
    }

    @GetMapping("/playlists")
    public ResponseEntity<List<Playlist>> getUserPlaylists() {
        List<Playlist> playlists = playlistService.getUserPlaylists();
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/playlists/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable Long id) {
        Playlist playlist = playlistService.getPlaylistById(id);
        return ResponseEntity.ok(playlist);
    }

    @PostMapping("/playlists")
    public ResponseEntity<Playlist> createPlaylist(@Valid @RequestBody PlaylistRequest playlistRequest) {
        Playlist playlist = playlistService.createPlaylist(playlistRequest);
        return ResponseEntity.ok(playlist);
    }

    @PutMapping("/playlists/{id}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Long id,
            @Valid @RequestBody PlaylistRequest playlistRequest) {
        Playlist playlist = playlistService.updatePlaylist(id, playlistRequest);
        return ResponseEntity.ok(playlist);
    }

    @DeleteMapping("/playlists/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/playlists/{playlistId}/songs/{songId}")
    public ResponseEntity<Playlist> addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        Playlist playlist = playlistService.addSongToPlaylist(playlistId, songId);
        return ResponseEntity.ok(playlist);
    }

    @DeleteMapping("/playlists/{playlistId}/songs/{songId}")
    public ResponseEntity<Playlist> removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        Playlist playlist = playlistService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.ok(playlist);
    }

    @GetMapping("/playlists/search")
    public ResponseEntity<List<Playlist>> searchPlaylistsByName(@RequestParam String name) {
        List<Playlist> playlists = playlistService.searchPlaylistsByName(name);
        return ResponseEntity.ok(playlists);
    }
}
