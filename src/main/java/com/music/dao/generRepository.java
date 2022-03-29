package com.music.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import com.music.entities.Genre;
import com.music.entities.User;

public interface generRepository extends JpaRepository<Genre, Integer> {
	
	@Query("select genre from Genre genre where genre.value = :value")
	public Genre getgenreByValue(@RequestParam("value") String value);

}
