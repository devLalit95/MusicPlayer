package com.example.MusicPlayer.service;


import com.example.MusicPlayer.dto.SongRequest;
import com.example.MusicPlayer.model.Song;
import com.example.MusicPlayer.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;

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

    public Song updateSong(Long id, SongRequest songRequest) {
        Song song = getSongById(id);
        song.setTitle(songRequest.getTitle());
        song.setArtist(songRequest.getArtist());
        song.setAlbum(songRequest.getAlbum());
        song.setFileUrl(songRequest.getFileUrl());
        song.setDurationSeconds(songRequest.getDurationSeconds());

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
}