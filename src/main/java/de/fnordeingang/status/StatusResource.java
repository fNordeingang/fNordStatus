package de.fnordeingang.status;

import de.fnordeingang.status.response.StatusResponse;
import twitter4j.TwitterException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes("*/*")
@Path("/")
public class StatusResource {
	@Inject
	private Status status;

	@Inject
	private Twitter twitter;

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

		return Response.ok().build();
	}
}
