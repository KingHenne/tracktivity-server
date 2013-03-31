package de.hliebau.tracktivity.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {

	@RequestMapping(value = "/{fragment}", method = RequestMethod.GET)
	public String catchAll(@PathVariable String fragment) {
		return fragment;
	}

}
