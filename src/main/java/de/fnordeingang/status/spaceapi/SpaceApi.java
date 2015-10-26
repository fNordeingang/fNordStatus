package de.fnordeingang.status.spaceapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.fnordeingang.status.Status;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@Data
@RequestScoped
@JsonIgnoreProperties({"status", "handler", "targetClass", "targetInstance"})
public class SpaceApi {
	@Inject
	private Status status;

	private String api = "0.13";
	private final String space = "fnordeingang";
	private String logo = "https://fnordeingang.de/logo.png";
	private String url = "https://fnordeingang.de";
	private Location location = new Location("Körnerstr. 72, 41464 Neuss, Germany", 1.2, 2.1);
	private Contact contact = new Contact("@fnordeingang");
	private IssueReportChannels issueReportChannels = new IssueReportChannels("twitter");
	private State state;

	@PostConstruct
	public void postConstruct() {
		state = new State(status.isOpen());
	}
}
