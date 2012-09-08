package de.hliebau.tracktivity.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.hliebau.tracktivity.domain.Activities;
import de.hliebau.tracktivity.domain.User;
import de.hliebau.tracktivity.service.UserService;

@Controller
@RequestMapping("/users")
public class UserRestService {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public @ResponseBody
	User getUser(@PathVariable String username) {
		return userService.retrieveUser(username, false);
	}

	@RequestMapping(value = "/{username}/activities", method = RequestMethod.GET)
	public @ResponseBody
	Activities getUserActivities(@PathVariable String username) {
		User user = userService.retrieveUser(username, true);
		return new Activities(user.getActivities());
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody
	List<User> getUsers() {
		return null;
	}
}
