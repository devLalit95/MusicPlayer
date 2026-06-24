package com.example.MusicPlayer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "song_play_history", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "song_id" }))
public class SongPlayHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(name = "play_count", nullable = false)
    private Long playCount = 0L;

    @Column(name = "last_played_at")
    private LocalDateTime lastPlayedAt;

    public SongPlayHistory() {
    }

    public SongPlayHistory(User user, Song song) {
        this.user = user;
        this.song = song;
        this.playCount = 0L;
        this.lastPlayedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }

    public LocalDateTime getLastPlayedAt() {
        return lastPlayedAt;
    }

    public void setLastPlayedAt(LocalDateTime lastPlayedAt) {
        this.lastPlayedAt = lastPlayedAt;
    }

    public void incrementPlayCount() {
        if (this.playCount == null) {
            this.playCount = 0L;
        }
        this.playCount++;
        this.lastPlayedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SongPlayHistory))
            return false;
        SongPlayHistory that = (SongPlayHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
