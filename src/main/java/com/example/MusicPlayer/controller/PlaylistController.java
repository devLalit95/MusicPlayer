package com.example.MusicPlayer.controller;

import com.example.MusicPlayer.dto.PlaylistRequest;
import com.example.MusicPlayer.model.Playlist;
import com.example.MusicPlayer.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public ResponseEntity<List<Playlist>> getUserPlaylists() {
        List<Playlist> playlists = playlistService.getUserPlaylists();
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable Long id) {
        Playlist playlist = playlistService.getPlaylistById(id);
        return ResponseEntity.ok(playlist);
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@Valid @RequestBody PlaylistRequest playlistRequest) {
        Playlist playlist = playlistService.createPlaylist(playlistRequest);
        return ResponseEntity.ok(playlist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Long id, @Valid @RequestBody PlaylistRequest playlistRequest) {
        Playlist playlist = playlistService.updatePlaylist(id, playlistRequest);
        return ResponseEntity.ok(playlist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Playlist> addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        Playlist playlist = playlistService.addSongToPlaylist(playlistId, songId);
        return ResponseEntity.ok(playlist);
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Playlist> removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        Playlist playlist = playlistService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.ok(playlist);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Playlist>> searchPlaylistsByName(@RequestParam String name) {
        List<Playlist> playlists = playlistService.searchPlaylistsByName(name);
        return ResponseEntity.ok(playlists);
    }
}