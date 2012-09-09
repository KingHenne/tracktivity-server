package de.hliebau.tracktivity.api;

import java.security.Principal;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.hliebau.tracktivity.api.domain.ThinActivity;
import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.User;
import de.hliebau.tracktivity.service.ActivityService;
import de.hliebau.tracktivity.service.UserService;

@Controller
@RequestMapping("/activities")
public class ActivityRestService {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private UserService userService;

	@RolesAllowed("ROLE_USER")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ThinActivity createActivity(@RequestBody Activity activity, Principal principal) {
		User authenticatedUser = userService.retrieveUser(principal.getName(), false);
		activity.setUser(authenticatedUser);
		activityService.createActivity(activity);
		return new ThinActivity(activity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Activity getActivity(@PathVariable Long id) {
		return activityService.retrieveActivityWithTrack(id);
	}

}
