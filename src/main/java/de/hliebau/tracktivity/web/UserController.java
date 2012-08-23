package de.hliebau.tracktivity.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.hliebau.tracktivity.domain.User;
import de.hliebau.tracktivity.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
	public String displayUserProfile(@PathVariable String username, Model model) {
		User user = userService.retrieveUser(username);
		if (user != null) {
			model.addAttribute(user);
		}
		return "user";
	}

	@RequestMapping(value = "/users")
	public String listUsers(Model model) {
		model.addAttribute(userService.getAllUsers());
		return "users";
	}

	@RequestMapping(value = "/user/new", method = RequestMethod.GET)
	public String registerUser(Model model) {
		model.addAttribute(new User());
		return "register";
	}

	@RequestMapping(value = "/user/new", method = RequestMethod.POST)
	public String registerUser(@Valid User user, BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			try {
				userService.createUser(user);
			} catch (DataIntegrityViolationException e) {
				bindingResult.addError(new FieldError("user", "username", "Please choose another user name, <em>"
						+ user.getUsername() + "</em> is already taken."));
			}
		}
		if (bindingResult.hasErrors()) {
			return "register";
		}
		return "redirect:" + user.getUsername();
	}

}
