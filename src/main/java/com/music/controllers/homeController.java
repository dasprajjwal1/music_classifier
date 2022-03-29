package com.music.controllers;

import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.music.dao.UserRepositry;
import com.music.entities.User;
import com.music.helper.message;

@Controller
public class homeController {

	@Autowired
	private UserRepositry userRepositry;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping(value = "/")
	public String home() {
		return "home";
	}

	@RequestMapping(path = "/signup")
	public String signup(Model model) {
		model.addAttribute("title", "signUp -Music Classifier");
		model.addAttribute("user", new User());

		return "signup";
	}

	@RequestMapping(path = "/register_user",method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") User user,@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			BindingResult result, HttpSession session) throws Exception
	{
		try {
		
		if(!agreement)
		{
			System.out.println("terms and conditions are not applied");
			throw new Exception("terms and conditions are not applied");
		}
		if(result.hasErrors())
		{
			System.out.println("Error "+result.toString());
			model.addAttribute("user",user);
			return "signup";
		}
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageUrl("deafult.png");
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		User result1 = this.userRepositry.save(user);
		System.out.println(result1);
		model.addAttribute("user", new User()); 
		// if the user is registered succesfully then blank user is
												// transfered.
		session.setAttribute("message", new message("successfully Registered!", "alert-success"));
	
		}
		catch (Exception e) {
		e.printStackTrace();
		model.addAttribute("user", user);
		session.setAttribute("message", new message("something went wrong " + e.getMessage(), "alert-danger"));
		return "signup";
	}
	return "signup";
	}
	
	@RequestMapping(path ="/signin")
	public String login(Model model)
	{
		model.addAttribute("title", "login- Music Classifier");
		return "login";
	}
	

}
