package de.hliebau.tracktivity.presentation;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import de.hliebau.tracktivity.domain.ActivityType;

public class UploadItem {

	private ActivityType activityType;

	private CommonsMultipartFile fileData;

	public ActivityType getActivityType() {
		return activityType;
	}

	public CommonsMultipartFile getFileData() {
		return fileData;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public void setFileData(CommonsMultipartFile fileData) {
		this.fileData = fileData;
	}

}
