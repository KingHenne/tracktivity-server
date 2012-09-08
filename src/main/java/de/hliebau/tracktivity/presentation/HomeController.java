package de.hliebau.tracktivity.presentation;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.service.ActivityService;

@Controller
public class HomeController {

	@Autowired
	private ActivityService activityService;

	@RequestMapping("/")
	public String showHomePage(Map<String, Object> model) {
		long start = System.currentTimeMillis();
		List<Activity> activities = activityService.getRecentActivities(50);
		long end = System.currentTimeMillis();
		model.put("fetchTime", end - start);
		model.put("activities", activities);
		return "home";
	}

}
