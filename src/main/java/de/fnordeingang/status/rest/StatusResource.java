package de.fnordeingang.status.rest;

import de.fnordeingang.status.StatusState;
import de.fnordeingang.status.Twitter;
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
	private StatusState statusState;

	@Inject
	private SpaceApi spaceApi;

	@Inject
	private Twitter twitter;

	@Inject
	private Logger logger;

	@GET
	public StatusResponse getStatusState() throws TwitterException {
		return new StatusResponse(
				statusState.isOpen(),
				statusState.getChangedAt());
	}

	@GET
	@Path("/plain")
	public String getStatusPlain() throws TwitterException {
		return String.valueOf(statusState.isOpen());
	}

	@POST
	@Path("/open")
	public StatusResponse open() {
		statusState.open();
		return new StatusResponse(
				statusState.isOpen(),
				statusState.getChangedAt());
	}

	@POST
	@Path("/close")
	public StatusResponse close() {
		statusState.close();
		return new StatusResponse(
				statusState.isOpen(),
				statusState.getChangedAt());
	}

	@GET
	@Path("/ping")
	public Response ping() {
		statusState.setLastPing(LocalDateTime.now());

		if(!statusState.isOpen()) {
			statusState.open();
		}

		logger.info("received ping");

		return Response.ok().build();
	}
}
