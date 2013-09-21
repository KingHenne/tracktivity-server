package de.hliebau.tracktivity.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.ActivityType;
import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.User;
import de.hliebau.tracktivity.service.ActivityService;
import de.hliebau.tracktivity.service.UserService;
import de.hliebau.tracktivity.util.JsonTrackSerializer;

@Controller
@RequestMapping("/activities")
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private UserService userService;

	private ObjectMapper createObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(new JsonTrackSerializer(Track.class));
		mapper.registerModule(module);
		return mapper;
	}

	@RequestMapping(value = "/{activityId}", method = RequestMethod.GET)
	public String displayActivity(@PathVariable Long activityId, Model model) {
		Activity activity = activityService.retrieveActivityWithTrack(activityId);
		if (activity != null) {
			model.addAttribute(activity);
			model.addAttribute("durationNetto", activity.getTrack().getDuration(false));
			model.addAttribute("durationBrutto", activity.getTrack().getDuration(true));
			// create JSON data to be used in JS
			ObjectMapper mapper = createObjectMapper();
			JsonNode trackTree = mapper.valueToTree(activity.getTrack());
			model.addAttribute("multiPolyline", trackTree.get("multiPolyline").toString());
			model.addAttribute("latLngBounds", trackTree.get("latLngBounds").toString());
		}
		return "activity";
	}

	private String getFileExtension(MultipartFile file) {
		String[] splittet = file.getOriginalFilename().split("\\.");
		return splittet[splittet.length - 1];
	}

	@RolesAllowed("ROLE_USER")
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String uploadActivity(Model model) {
		model.addAttribute(new UploadItem());
		return "upload";
	}

	@RolesAllowed("ROLE_USER")
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String uploadActivity(UploadItem uploadItem, BindingResult bindingResult, Principal principal) {
		try {
			User currentUser = userService.retrieveUser(principal.getName(), false);
			ActivityType type = uploadItem.getActivityType();
			Activity activity = null;
			CommonsMultipartFile file = uploadItem.getFileData();
			validateFile(file);
			InputStream in = file.getInputStream();
			String fileExtension = getFileExtension(file);
			if ("gpx".equals(fileExtension)) {
				activity = activityService.importGpxAsUserActivity(in, currentUser, type);
			} else if ("tcx".equals(fileExtension)) {
				activity = activityService.importTcxAsUserActivity(in, currentUser, type);
			}
			in.close();
			return "redirect:" + activity.getId();
		} catch (FileUploadException e) {
			bindingResult.addError(new FieldError("uploadItem", "fileData", e.getMessage()));
		} catch (IOException e) {
			bindingResult.addError(new FieldError("uploadItem", "fileData", e.getMessage()));
		}
		return "upload";
	}

	private void validateFile(MultipartFile file) {
		if (file.isEmpty()) {
			throw new FileUploadException("You need to select a GPX or TCX file.");
		}
		String fileExtension = getFileExtension(file);
		String contentType = file.getContentType();
		if (!("gpx".equals(fileExtension) && contentType.startsWith("application/octet-stream"))
				&& !("tcx".equals(fileExtension) && contentType.startsWith("application/tcx+xml"))) {
			throw new FileUploadException("Only GPX and TCX files are supported.");
		}
	}

}
