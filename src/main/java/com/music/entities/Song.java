package com.music.entities;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Song {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sId;
	private String FileName;
	private String FileType;
	private String instrumentalness;
	private String energy;
	private String loudness;
	private String acousticness;
	private String duration_ms;
	@Lob
	private byte[] data;
	@ManyToOne
	private User user;

	public Song(int sId, String fileName, String fileType, String instrumentalness, String energy, String loudness,
			String acousticness, String duration_ms, byte[] data, User user) {
		super();
		this.sId = sId;
		FileName = fileName;
		FileType = fileType;
		this.instrumentalness = instrumentalness;
		this.energy = energy;
		this.loudness = loudness;
		this.acousticness = acousticness;
		this.duration_ms = duration_ms;
		this.data = data;
		this.user = user;
	}

	public int getsId() {
		return sId;
	}

	public void setsId(int sId) {
		this.sId = sId;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public String getFileType() {
		return FileType;
	}

	public void setFileType(String fileType) {
		FileType = fileType;
	}

	public String getInstrumentalness() {
		return instrumentalness;
	}

	public void setInstrumentalness(String instrumentalness) {
		this.instrumentalness = instrumentalness;
	}

	public String getEnergy() {
		return energy;
	}

	public void setEnergy(String energy) {
		this.energy = energy;
	}

	public String getLoudness() {
		return loudness;
	}

	public void setLoudness(String loudness) {
		this.loudness = loudness;
	}

	public String getAcousticness() {
		return acousticness;
	}

	public void setAcousticness(String acousticness) {
		this.acousticness = acousticness;
	}

	public String getDuration_ms() {
		return duration_ms;
	}

	public void setDuration_ms(String duration_ms) {
		this.duration_ms = duration_ms;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public User getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "Song [sId=" + sId + ", FileName=" + FileName + ", FileType=" + FileType + ", instrumentalness="
				+ instrumentalness + ", energy=" + energy + ", loudness=" + loudness + ", acousticness=" + acousticness
				+ ", duration_ms=" + duration_ms + ", data=" + Arrays.toString(data) + ", user=" + user + "]";
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Song() {
		super();
		
	}
	

}