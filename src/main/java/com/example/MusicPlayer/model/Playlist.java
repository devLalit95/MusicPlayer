package com.example.MusicPlayer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "playlists")
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Playlist name is required")
    @Size(max = 100, message = "Playlist name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // CORRECTED ManyToMany mapping with composite key
    @ManyToMany
    @JoinTable(
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"playlist_id", "song_id"})
            }
    )
    private List<Song> songs = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    // Business methods with exception handling
    public void addSong(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("Song cannot be null");
        }

        if (this.songs == null) {
            this.songs = new ArrayList<>();
        }

        // Check if song already exists in playlist to avoid duplicates
        boolean songExists = this.songs.stream()
                .anyMatch(existingSong -> existingSong.getId() != null &&
                        existingSong.getId().equals(song.getId()));

        if (!songExists) {
            this.songs.add(song);
            if (song.getPlaylists() != null) {
                song.getPlaylists().add(this);
            }
        }
    }

    public void removeSong(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("Song cannot be null");
        }

        if (this.songs != null) {
            boolean removed = this.songs.removeIf(s ->
                    s.getId() != null && s.getId().equals(song.getId()));

            if (removed && song.getPlaylists() != null) {
                song.getPlaylists().removeIf(p ->
                        p.getId() != null && p.getId().equals(this.id));
            }
        }
    }

    // Utility methods
    public boolean containsSong(Song song) {
        if (song == null || song.getId() == null || this.songs == null) {
            return false;
        }
        return this.songs.stream()
                .anyMatch(s -> s.getId() != null && s.getId().equals(song.getId()));
    }

    // Validation method
    public void validate() {
        if (this.name == null || this.name.trim().isEmpty()) {
            throw new IllegalStateException("Playlist name cannot be empty");
        }
        if (this.user == null) {
            throw new IllegalStateException("Playlist must belong to a user");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", songsCount=" + (songs != null ? songs.size() : 0) +
                '}';
    }
}