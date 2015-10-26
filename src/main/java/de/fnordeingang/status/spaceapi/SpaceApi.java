package de.fnordeingang.status.spaceapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.fnordeingang.status.Status;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Data
@ApplicationScoped
@JsonIgnoreProperties({"status", "handler", "targetClass", "targetInstance", "spaceApi"})
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes("*/*")
@Path("/")
public class SpaceApi {
	@Inject
	private Status status;

	private String api = "0.13";
	private final String space = "fnordeingang";
	private String logo = "https://fnordeingang.de/logo.png";
	private String url = "https://fnordeingang.de";
	private Location location = new Location("Körnerstr. 72, 41464 Neuss, Germany", 51.1862, 6.69232);
	private Contact contact = new Contact("@fnordeingang");
	private IssueReportChannels issueReportChannels = new IssueReportChannels("twitter");
	private State state;

	@PostConstruct
	public void postConstruct() {
		state = new State(status.isOpen());
	}

	@GET
	@Path("/spaceapi.json")
	public SpaceApi getSpaceApi() {
		return this;
	}
}
