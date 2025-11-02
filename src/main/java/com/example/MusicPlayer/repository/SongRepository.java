package com.example.MusicPlayer.repository;


import com.example.MusicPlayer.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    List<Song> findByTitleContainingIgnoreCase(String title);

    List<Song> findByArtistContainingIgnoreCase(String artist);

    List<Song> findByAlbumContainingIgnoreCase(String album);
}