package com.example.MusicPlayer.service;

import com.example.MusicPlayer.dto.SongRequest;
import com.example.MusicPlayer.exception.ResourceNotFoundException;
import com.example.MusicPlayer.model.Song;
import com.example.MusicPlayer.model.SongPlayHistory;
import com.example.MusicPlayer.model.User;
import com.example.MusicPlayer.repository.SongPlayHistoryRepository;
import com.example.MusicPlayer.repository.SongRepository;
import com.example.MusicPlayer.repository.UserRepository;
import com.example.MusicPlayer.security.CloudinaryService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongService implements SongServiceInterface {

    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final SongPlayHistoryRepository songPlayHistoryRepository;
    private final CloudinaryService cloudinaryService;

    public SongService(SongRepository songRepository,
            UserRepository userRepository,
            SongPlayHistoryRepository songPlayHistoryRepository,
            CloudinaryService cloudinaryService) {
        this.songRepository = songRepository;
        this.userRepository = userRepository;
        this.songPlayHistoryRepository = songPlayHistoryRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @Override
    public Song getSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + id));
    }

    @Override
    public Song createSong(SongRequest songRequest) {
        Song song = new Song();
        song.setTitle(songRequest.getTitle());
        song.setArtist(songRequest.getArtist());
        song.setAlbum(songRequest.getAlbum());
        song.setFileUrl(songRequest.getFileUrl());
        song.setDurationSeconds(songRequest.getDurationSeconds());
        return songRepository.save(song);
    }

    @Override
    public Song updateSong(Long id, SongRequest songRequest) {
        Song song = getSongById(id);
        if (songRequest.getTitle() != null && !songRequest.getTitle().equals(song.getTitle())) {
            song.setTitle(songRequest.getTitle());
        }
        if (songRequest.getArtist() != null && !songRequest.getArtist().equals(song.getArtist())) {
            song.setArtist(songRequest.getArtist());
        }
        if (songRequest.getAlbum() != null && !songRequest.getAlbum().equals(song.getAlbum())) {
            song.setAlbum(songRequest.getAlbum());
        }
        if (songRequest.getFileUrl() != null && !songRequest.getFileUrl().equals(song.getFileUrl())) {
            song.setFileUrl(songRequest.getFileUrl());
        }
        if (songRequest.getDurationSeconds() != null
                && !songRequest.getDurationSeconds().equals(song.getDurationSeconds())) {
            song.setDurationSeconds(songRequest.getDurationSeconds());
        }
        return songRepository.save(song);
    }

    @Override
    public void deleteSong(Long id) {
        Song song = getSongById(id);
        songRepository.delete(song);
    }

    @Override
    public List<Song> searchSongsByTitle(String title) {
        return songRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Song> searchSongsByArtist(String artist) {
        return songRepository.findByArtistContainingIgnoreCase(artist);
    }

    @Override
    public Song uploadSong(MultipartFile file, String title, String artist,
            String album, Integer durationSeconds) throws IOException {
        String fileUrl = cloudinaryService.uploadSong(file);
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setAlbum(album);
        song.setFileUrl(fileUrl);
        song.setDurationSeconds(durationSeconds);
        song.setCreatedAt(LocalDateTime.now());
        return songRepository.save(song);
    }

    @Override
    public void likeSong(Long songId) {
        User user = getCurrentUser();
        Song song = getSongById(songId);
        user.addLikedSong(song);
        userRepository.save(user);
    }

    @Override
    public void dislikeSong(Long songId) {
        User user = getCurrentUser();
        Song song = getSongById(songId);
        user.removeLikedSong(song);
        userRepository.save(user);
    }

    @Override
    public List<Song> getUserLikedSongs() {
        User user = getCurrentUser();
        return new ArrayList<>(user.getLikedSongs());
    }

    @Override
    public List<Song> getUserFrequentlyPlayedSongs() {
        User user = getCurrentUser();
        return songPlayHistoryRepository.findTop10ByUserIdOrderByPlayCountDesc(user.getId()).stream()
                .map(SongPlayHistory::getSong)
                .toList();
    }

    @Override
    public Song recordSongPlay(Long songId) {
        User user = getCurrentUser();
        Song song = getSongById(songId);
        SongPlayHistory history = songPlayHistoryRepository.findByUserIdAndSongId(user.getId(), songId)
                .orElseGet(() -> new SongPlayHistory(user, song));
        history.incrementPlayCount();
        songPlayHistoryRepository.save(history);
        return song;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
