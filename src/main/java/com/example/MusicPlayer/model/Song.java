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
@Table(name = "songs")
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Song title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Artist name is required")
    @Size(max = 100, message = "Artist name must not exceed 100 characters")
    private String artist;

    @Size(max = 100, message = "Album name must not exceed 100 characters")
    private String album;

    @NotBlank(message = "File URL is required")
    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Corrected ManyToMany mapping
    @ManyToMany(mappedBy = "songs")
    private List<Playlist> playlists = new ArrayList<>();

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    // Business methods
    public void addToPlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist cannot be null");
        }

        if (this.playlists == null) {
            this.playlists = new ArrayList<>();
        }

        boolean playlistExists = this.playlists.stream()
                .anyMatch(p -> p.getId() != null && p.getId().equals(playlist.getId()));

        if (!playlistExists) {
            this.playlists.add(playlist);
            if (playlist.getSongs() != null) {
                playlist.getSongs().add(this);
            }
        }
    }

    public void removeFromPlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist cannot be null");
        }

        if (this.playlists != null) {
            boolean removed = this.playlists.removeIf(p ->
                    p.getId() != null && p.getId().equals(playlist.getId()));

            if (removed && playlist.getSongs() != null) {
                playlist.getSongs().removeIf(s ->
                        s.getId() != null && s.getId().equals(this.id));
            }
        }
    }

    // Validation method
    public void validate() {
        if (this.title == null || this.title.trim().isEmpty()) {
            throw new IllegalStateException("Song title cannot be empty");
        }
        if (this.artist == null || this.artist.trim().isEmpty()) {
            throw new IllegalStateException("Artist name cannot be empty");
        }
        if (this.fileUrl == null || this.fileUrl.trim().isEmpty()) {
            throw new IllegalStateException("File URL cannot be empty");
        }
    }

    // Equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id) &&
                Objects.equals(title, song.title) &&
                Objects.equals(artist, song.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, artist);
    }

    // toString method
    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", durationSeconds=" + durationSeconds +
                ", createdAt=" + createdAt +
                ", playlistsCount=" + (playlists != null ? playlists.size() : 0) +
                '}';
    }
}