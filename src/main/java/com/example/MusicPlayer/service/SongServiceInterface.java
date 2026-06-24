package com.example.MusicPlayer.service;

import com.example.MusicPlayer.dto.SongRequest;
import com.example.MusicPlayer.model.Song;

import java.util.List;

public interface SongServiceInterface {

    List<Song> getAllSongs();

    Song getSongById(Long id);

    Song createSong(SongRequest songRequest);

    Song updateSong(Long id, SongRequest songRequest);

    void deleteSong(Long id);

    List<Song> searchSongsByTitle(String title);

    List<Song> searchSongsByArtist(String artist);

    Song uploadSong(org.springframework.web.multipart.MultipartFile file, String title, String artist,
            String album, Integer durationSeconds) throws java.io.IOException;

    void likeSong(Long songId);

    void dislikeSong(Long songId);

    List<Song> getUserLikedSongs();

    List<Song> getUserFrequentlyPlayedSongs();

    Song recordSongPlay(Long songId);
}
