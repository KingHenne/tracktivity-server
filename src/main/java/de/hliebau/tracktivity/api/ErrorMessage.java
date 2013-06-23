package de.hliebau.tracktivity.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {

	private final String error;

	public ErrorMessage(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}
}
