package de.hliebau.tracktivity.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.service.ActivityService;

@Controller
@RequestMapping({ "/activity" })
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	@RequestMapping(value = "/{activityId}", method = RequestMethod.GET)
	public String displayActivty(@PathVariable Long activityId, Model model) {
		Activity activity = activityService.retrieveActivity(activityId);
		if (activity != null) {
			model.addAttribute(activity);
		}
		return "activity";
	}

}
