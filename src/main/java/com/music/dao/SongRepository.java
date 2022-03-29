package com.music.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.music.entities.Song;

public interface SongRepository extends JpaRepository<Song, Integer>{
	@Query("from Song as s where s.user.id=:userId")
	public List<Song> findAllSongByUserId(@Param("userId") int userId);

}
