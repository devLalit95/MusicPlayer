package com.example.MusicPlayer.service;


import com.example.MusicPlayer.dto.PlaylistRequest;
import com.example.MusicPlayer.model.Playlist;
import com.example.MusicPlayer.model.Song;
import com.example.MusicPlayer.model.User;
import com.example.MusicPlayer.repository.PlaylistRepository;
import com.example.MusicPlayer.repository.SongRepository;
import com.example.MusicPlayer.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public PlaylistService(PlaylistRepository playlistRepository, UserRepository userRepository,
                           SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public List<Playlist> getUserPlaylists() {
        User currentUser = getCurrentUser();
        return playlistRepository.findByUserId(currentUser.getId());
    }

    public Playlist getPlaylistById(Long id) {
        User currentUser = getCurrentUser();
        return playlistRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + id));
    }

    public Playlist createPlaylist(PlaylistRequest playlistRequest) {
        User currentUser = getCurrentUser();

        Playlist playlist = new Playlist();
        playlist.setName(playlistRequest.getName());
        playlist.setDescription(playlistRequest.getDescription());
        playlist.setUser(currentUser);

        return playlistRepository.save(playlist);
    }

    public Playlist updatePlaylist(Long id, PlaylistRequest playlistRequest) {
        Playlist playlist = getPlaylistById(id);
        playlist.setName(playlistRequest.getName());
        playlist.setDescription(playlistRequest.getDescription());

        return playlistRepository.save(playlist);
    }

    public void deletePlaylist(Long id) {
        Playlist playlist = getPlaylistById(id);
        playlistRepository.delete(playlist);
    }

    public Playlist addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = getPlaylistById(playlistId);
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + songId));

        if (!playlist.getSongs().contains(song)) {
            playlist.addSong(song);
            return playlistRepository.save(playlist);
        }

        return playlist;
    }

    public Playlist removeSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = getPlaylistById(playlistId);
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + songId));

        if (playlist.getSongs().contains(song)) {
            playlist.removeSong(song);
            return playlistRepository.save(playlist);
        }

        return playlist;
    }

    public List<Playlist> searchPlaylistsByName(String name) {
        User currentUser = getCurrentUser();
        return playlistRepository.findByNameContainingIgnoreCaseAndUserId(name, currentUser.getId());
    }
}