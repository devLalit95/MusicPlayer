package com.example.MusicPlayer.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "playlist_songs")
@IdClass(PlaylistSongId.class)
public class PlaylistSong {

    @Id
    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @Id
    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @Column(name = "added_at")
    private LocalDateTime addedAt = LocalDateTime.now();

    // constructors, getters, setters
}
