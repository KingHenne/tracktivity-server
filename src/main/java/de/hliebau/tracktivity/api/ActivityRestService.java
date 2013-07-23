package de.hliebau.tracktivity.api;

import java.security.Principal;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.hliebau.tracktivity.api.domain.ThinActivities;
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
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ThinActivity createActivity(@RequestBody Activity activity, Principal principal) {
		User authenticatedUser = userService.retrieveUser(principal.getName(), false);
		activity.setUser(authenticatedUser);
		activityService.createActivity(activity);
		return new ThinActivity(activity);
	}

	@RolesAllowed("ROLE_USER")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteActivity(@PathVariable Long id, Principal principal, HttpServletResponse response) {
		User authenticatedUser = userService.retrieveUser(principal.getName(), false);
		Activity activity = activityService.retrieveActivity(id);
		if (activity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else if (!activity.getUser().equals(authenticatedUser)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} else {
			activityService.deleteActivity(activity);
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<ThinActivity> getActivities(@RequestParam(defaultValue = "50") int count) {
		return new ThinActivities(activityService.getRecentActivities(count)).getActivities();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Activity getActivity(@PathVariable Long id) {
		return activityService.retrieveActivityWithTrack(id);
	}

}
