package de.hliebau.tracktivity.presentation;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.service.TrackService;

@Controller
public class HomeController {

	@Autowired
	private TrackService trackService;

	@RequestMapping({ "/" })
	public String showHomePage(Map<String, Object> model) {
		long start = System.currentTimeMillis();
		List<Track> tracks = trackService.getRecentTracks(50);
		long end = System.currentTimeMillis();
		model.put("fetchTime", end - start);
		model.put("tracks", tracks);
		return "home";
	}

}
