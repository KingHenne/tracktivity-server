package de.hliebau.tracktivity.presentation;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, Principal principal) {
		if (principal != null) {
			return "redirect:welcome";
		}
		return "login";
	}

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginFailed(Model model, HttpServletRequest req) {
		Object authException = req.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (authException != null) {
			model.addAttribute("error", true);
			return "login";
		}
		return "redirect:login";
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String welcome(Model model, Principal principal) {
		if (principal == null) {
			return "redirect:login";
		}
		String name = principal.getName();
		model.addAttribute("username", name);
		return "welcome";
	}

}
