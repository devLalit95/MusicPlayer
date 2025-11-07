package com.example.MusicPlayer.service;


import com.example.MusicPlayer.dto.SongRequest;
import com.example.MusicPlayer.model.Song;
import com.example.MusicPlayer.repository.SongRepository;
import com.example.MusicPlayer.security.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;
    CloudinaryService cloudinaryService;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + id));
    }

    public Song createSong(SongRequest songRequest) {
        Song song = new Song();
        song.setTitle(songRequest.getTitle());
        song.setArtist(songRequest.getArtist());
        song.setAlbum(songRequest.getAlbum());
        song.setFileUrl(songRequest.getFileUrl());
        song.setDurationSeconds(songRequest.getDurationSeconds());

        return songRepository.save(song);
    }
// put mapping with partial update
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
        if (songRequest.getDurationSeconds() != null && !songRequest.getDurationSeconds().equals(song.getDurationSeconds())) {
            song.setDurationSeconds(songRequest.getDurationSeconds());
        }

        return songRepository.save(song);
    }

    public void deleteSong(Long id) {
        Song song = getSongById(id);
        songRepository.delete(song);
    }

    public List<Song> searchSongsByTitle(String title) {
        return songRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Song> searchSongsByArtist(String artist) {
        return songRepository.findByArtistContainingIgnoreCase(artist);
    }



    public Song uploadSong(MultipartFile file, String title, String artist,
                           String album, Integer durationSeconds) throws IOException {

        // 1️⃣ Upload file to Cloudinary
        String fileUrl = cloudinaryService.uploadSong(file);


        // 2️⃣ Create and save song record
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setAlbum(album);
        song.setFileUrl(fileUrl);
        song.setDurationSeconds(durationSeconds);
        song.setCreatedAt(LocalDateTime.now());

        return songRepository.save(song);
    }
}