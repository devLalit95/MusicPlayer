package com.example.MusicPlayer.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PlaylistSongId implements Serializable {
    private Long playlist;
    private Long song;
}