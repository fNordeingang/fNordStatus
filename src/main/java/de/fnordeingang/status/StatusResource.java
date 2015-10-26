package de.fnordeingang.status;

import de.fnordeingang.status.response.StatusResponse;
import de.fnordeingang.status.spaceapi.SpaceApi;
import twitter4j.TwitterException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes("*/*")
@Path("/")
public class StatusResource {
	@Inject
	private Status status;

	@Inject
	private SpaceApi spaceApi;

	@Inject
	private Twitter twitter;

	@Inject
	private Logger logger;

	@GET
	public StatusResponse getStatus() throws TwitterException {
		return new StatusResponse(
				status.isOpen(),
				status.getChangedAt());
	}

	@GET
	@Path("/plain")
	public String getStatusPlain() throws TwitterException {
		return String.valueOf(status.isOpen());
	}

	@POST
	@Path("/open")
	public StatusResponse open() {
		status.open();
		return new StatusResponse(
				status.isOpen(),
				status.getChangedAt());
	}

	@POST
	@Path("/close")
	public StatusResponse close() {
		status.close();
		return new StatusResponse(
				status.isOpen(),
				status.getChangedAt());
	}

	@GET
	@Path("/ping")
	public Response ping() {
		status.setLastPing(LocalDateTime.now());

		if(!status.isOpen()) {
			status.open();
		}

		logger.info("received ping");

		return Response.ok().build();
	}

	@GET
	@Path("/spaceapi.json")
	public SpaceApi getSpaceApi() {
		return spaceApi;
	}
}
