package de.hliebau.tracktivity.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.service.ActivityService;

@Controller
@RequestMapping("/activities")
public class ActivityRestService {

	@Autowired
	private ActivityService activityService;

	@RequestMapping(value = "/{id}")
	@ResponseBody
	public Activity getActivity(@PathVariable Long id) {
		return activityService.retrieveActivityWithTrack(id);
	}

}
