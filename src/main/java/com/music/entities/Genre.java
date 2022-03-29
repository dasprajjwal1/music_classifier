package com.music.entities;

import javax.persistence.Entity;


@Entity
public class Genre {
	@javax.persistence.Id	
	private int Id;
	private String value;
	private String type;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Genre() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Genre(int id, String value, String type) {
		super();
		Id = id;
		this.value = value;
		this.type = type;
	}

	@Override
	public String toString() {
		return "Genre [Id=" + Id + ", value=" + value + ", type=" + type + "]";
	}
	

	

}
