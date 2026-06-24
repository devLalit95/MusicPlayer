package com.example.MusicPlayer.service;

import com.example.MusicPlayer.dto.PlaylistRequest;
import com.example.MusicPlayer.model.Playlist;

import java.util.List;

public interface PlaylistServiceInterface {

    List<Playlist> getUserPlaylists();

    Playlist getPlaylistById(Long id);

    Playlist createPlaylist(PlaylistRequest playlistRequest);

    Playlist updatePlaylist(Long id, PlaylistRequest playlistRequest);

    void deletePlaylist(Long id);

    Playlist addSongToPlaylist(Long playlistId, Long songId);

    Playlist removeSongFromPlaylist(Long playlistId, Long songId);

    List<Playlist> searchPlaylistsByName(String name);
}
