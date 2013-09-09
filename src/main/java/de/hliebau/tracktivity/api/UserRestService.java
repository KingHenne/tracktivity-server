package de.hliebau.tracktivity.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.hliebau.tracktivity.api.domain.ThinActivity;
import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.User;
import de.hliebau.tracktivity.service.UserService;

@Controller
@RequestMapping("/users")
public class UserRestService {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	@ResponseBody
	public User getUser(@PathVariable String username) {
		return userService.retrieveUser(username, false);
	}

	@RequestMapping(value = "/{username}/activities", method = RequestMethod.GET)
	@ResponseBody
	public List<ThinActivity> getUserActivities(@PathVariable String username, HttpServletResponse response) {
		User user = userService.retrieveUser(username, true);
		List<ThinActivity> activities = null;
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			activities = new ArrayList<ThinActivity>();
			for (Activity activity : user.getActivities()) {
				activities.add(new ThinActivity(activity));
			}
		}
		return activities;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<User> getUsers() {
		return userService.getAllUsers();
	}
}
