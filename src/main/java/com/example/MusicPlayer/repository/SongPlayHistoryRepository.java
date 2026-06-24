package com.example.MusicPlayer.repository;

import com.example.MusicPlayer.model.SongPlayHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongPlayHistoryRepository extends JpaRepository<SongPlayHistory, Long> {

    Optional<SongPlayHistory> findByUserIdAndSongId(Long userId, Long songId);

    List<SongPlayHistory> findTop10ByUserIdOrderByPlayCountDesc(Long userId);

    List<SongPlayHistory> findByUserIdOrderByPlayCountDesc(Long userId);
}
