package de.hliebau.tracktivity.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.hliebau.tracktivity.service.TrackService;

@Controller
public class HomeController {

	@Autowired
	private TrackService trackService;

	@RequestMapping({ "/" })
	public String showHomePage(Map<String, Object> model) {
		model.put("headline", "Tracks");
		model.put("tracks", trackService.getRecentTracks(10));
		return "home";
	}

}
