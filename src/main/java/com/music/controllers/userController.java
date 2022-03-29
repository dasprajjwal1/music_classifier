package com.music.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpClient.Redirect;

import org.springframework.http.HttpHeaders;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

import org.apache.catalina.connector.Response;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.dao.SongRepository;
import com.music.dao.UserRepositry;
import com.music.dao.generRepository;
import com.music.entities.User;

import ch.qos.logback.core.subst.Parser;

import com.music.entities.Genre;
import com.music.entities.Song;

@Controller
@RequestMapping("/user")
public class userController {

	@Autowired
	private UserRepositry userRepo;

	@Autowired
	private SongRepository songrepo;

	@Autowired
	private generRepository generrepo;

	@ModelAttribute
	public void commonData(Model model, Principal principal) {
		String name = principal.getName();
		User user = this.userRepo.getUserByUsername(name);
		model.addAttribute("user", user);
	}
	
	@RequestMapping("/playlist")
	public String playSong()
	{
		return "normal/Playsongs";
	}
	

	@RequestMapping("/index")
	public String user_dashboard(Model model) {

		model.addAttribute("title", "userdashboard-MusicClassifier");

		// return "normal/user_dashboard";
		return "redirect:show-songs";
	}

//used to display the upload form to the user
	@RequestMapping("/upload_form")
	public String Upload_form(Model model) {
		model.addAttribute("title", "MusicClassifier-UploadForm");
		model.addAttribute("song", new Song());
		return "normal/song-upload-form";
	}

	// used to uplaod the file and their attributes that are saved in the database
	// and used to classify
	// the songs .

	@PostMapping("/add-song")
	public String SongUpload(@ModelAttribute Song song, @RequestParam("files") MultipartFile file, Model model,
			Principal principal) throws IOException {
		System.out.println(song);

		String username = principal.getName();
		User user = this.userRepo.getUserByUsername(username);
		song.setUser(user);
		song.setFileName(file.getOriginalFilename());
		song.setFileType(file.getContentType());
		song.setData(file.getBytes());
		user.getSongs().add(song);
		// User result = this.userRepo.save(user);
		Song result = this.songrepo.save(song);
		System.out.println("saved succesfully " + result);

		return "redirect:show-songs";
	}

	// method to display the songs uploaded by the user saved in the database.
	@RequestMapping(path = "/show-songs")
	public String listOfSongs(Model model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("title", "MusicClassifier-SongList");
		User user = this.userRepo.getUserByUsername(name);
		List<Song> songlist = this.songrepo.findAllSongByUserId(user.getId());
		model.addAttribute("songs", songlist);
		return "normal/user_dashboard";
	}
	
	
	//delete the song using songId
	@RequestMapping(path = "/delete/{songid}")
	public String DeleteSong(@PathVariable Integer songid)
	{
		Song song = songrepo.getById(songid);
		this.songrepo.delete(song);
		System.out.println("deleted successfully!!");
		return "redirect:/user/show-songs";
	}

	// used to download the file from the database using the particular songid.
	@GetMapping(path = "/download/{songid}")
	public ResponseEntity<ByteArrayResource> DownloadSong(@PathVariable Integer songid) {

		Song songByid = this.songrepo.getById(songid);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(songByid.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename=\"" + songByid.getFileName() + "\"")
				.body(new ByteArrayResource(songByid.getData()));
	}
	
//classify using songid
	@RequestMapping(path = "/classify/{songid}")
	public String ClassifySong(@PathVariable Integer songid, Model model) throws JsonProcessingException {
		Song song = this.songrepo.getById(songid);
		Song s1 = new Song();
		s1.setInstrumentalness(song.getInstrumentalness());
		s1.setEnergy(song.getEnergy());
		s1.setLoudness(song.getLoudness());
		s1.setAcousticness(song.getAcousticness());
		s1.setDuration_ms(song.getDuration_ms());
		s1.setFileName(song.getFileName());
		model.addAttribute("song",s1);
		// Converting the Object to JSONString
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(s1);
		System.out.println(jsonString);

		try {
			FileWriter fw = new FileWriter("D:\\Data\\testout.json");
			fw.write(jsonString);
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Success...");

		RestTemplate restTemaplate = new RestTemplate();
		String forObject = restTemaplate.getForObject("http://127.0.0.1:5000/classify", String.class);
		System.out.println(forObject);
		System.out.println(forObject.getClass());
		if (forObject.equals("[1]")) {
			
			model.addAttribute("genre", "Dark Trap");
		} else if (forObject.equals("[2]")) {
			model.addAttribute("genre", "Underground Rap");
		} else if (forObject.equals("[3]")) {
			model.addAttribute("genre", "Trap Metal");
		} else if (forObject.equals("[4]")) {
			model.addAttribute("genre", "Emo");
		} else if (forObject.equals("[5]")) {
			model.addAttribute("genre", "Rap");
		} else if (forObject.equals("[6]")) {
			model.addAttribute("genre", "RnB");
		} else if (forObject.equals("[7]")) {
			model.addAttribute("genre", "Pop");
		} else if (forObject.equals("[8]")) {
			model.addAttribute("genre", "Hiphop");
		} else if (forObject.equals("[9]")) {
			model.addAttribute("genre", "techhouse");
		} else if (forObject.equals("[10]")) {
			model.addAttribute("genre", "techno");
		} else if (forObject.equals("[11]")) {
			model.addAttribute("genre", "trance");
		} else if (forObject.equals("[12]")) {
			model.addAttribute("genre", "psy11");
		} else if (forObject.equals("[13]")) {
			model.addAttribute("genre", "t5");
		} else if (forObject.equals("[14]")) {
			model.addAttribute("genre", "dnb");
		} else if (forObject.equals("[15]")) {
			model.addAttribute("genre", "hardstyle");
		} else {
			model.addAttribute("genre", "not Found");
		}

		return "normal/classify";
	}
	
	
	
	//classifying using the form attribute.
	@PostMapping("/classifyattribute")
	public String ClassifySongAtt(Model model,@ModelAttribute("song") Song song) throws JsonProcessingException {
		Song s1 = new Song();
		s1.setInstrumentalness(song.getInstrumentalness());
		s1.setEnergy(song.getEnergy());
		s1.setLoudness(song.getLoudness());
		s1.setAcousticness(song.getAcousticness());
		s1.setDuration_ms(song.getDuration_ms());
		
		model.addAttribute("song",s1);
		// Converting the Object to JSONString
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(s1);
		System.out.println(jsonString);

		try {
			FileWriter fw = new FileWriter("D:\\Data\\testout.json");
			fw.write(jsonString);
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Success...");

		RestTemplate restTemaplate = new RestTemplate();
		String forObject = restTemaplate.getForObject("http://127.0.0.1:5000/classify", String.class);
		System.out.println(forObject);
		System.out.println(forObject.getClass());
		if (forObject.equals("[1]")) {
			
			model.addAttribute("genre", "Dark Trap");
		} else if (forObject.equals("[2]")) {
			model.addAttribute("genre", "Underground Rap");
		} else if (forObject.equals("[3]")) {
			model.addAttribute("genre", "Trap Metal");
		} else if (forObject.equals("[4]")) {
			model.addAttribute("genre", "Emo");
		} else if (forObject.equals("[5]")) {
			model.addAttribute("genre", "Rap");
		} else if (forObject.equals("[6]")) {
			model.addAttribute("genre", "RnB");
		} else if (forObject.equals("[7]")) {
			model.addAttribute("genre", "Pop");
		} else if (forObject.equals("[8]")) {
			model.addAttribute("genre", "Hiphop");
		} else if (forObject.equals("[9]")) {
			model.addAttribute("genre", "techhouse");
		} else if (forObject.equals("[10]")) {
			model.addAttribute("genre", "techno");
		} else if (forObject.equals("[11]")) {
			model.addAttribute("genre", "trance");
		} else if (forObject.equals("[12]")) {
			model.addAttribute("genre", "psy11");
		} else if (forObject.equals("[13]")) {
			model.addAttribute("genre", "t5");
		} else if (forObject.equals("[14]")) {
			model.addAttribute("genre", "dnb");
		} else if (forObject.equals("[15]")) {
			model.addAttribute("genre", "hardstyle");
		} else {
			model.addAttribute("genre", "not Found");
		}
		model.addAttribute("title", "MusicClassifier-ClassifyAtrribute");
		model.addAttribute("song", new Song());
		return "normal/attributeclassify";
	}
	
	@RequestMapping("/classify")
	public String classifyAttribute(Model model)
	{
		model.addAttribute("title", "MusicClassifier-UploadForm");
		model.addAttribute("song", new Song());
		return "normal/songclassifyform";
	}

	@RequestMapping("/accuracy")
	public String Accuracy(Model model) {

		RestTemplate rt = new RestTemplate();
		String accuracy = rt.getForObject("http://127.0.0.1:5000/accuracy", String.class);
		model.addAttribute("accuracy", accuracy);

		return "normal/accuracy";
	}

	@RequestMapping(path="/display_profile")
	public String DisplayProfile(Model model)
	{	
		model.addAttribute("title","musicClassifier-Profile");
		return "normal/profile";
	}
	
}
