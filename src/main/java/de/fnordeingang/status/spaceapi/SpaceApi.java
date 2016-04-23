package de.fnordeingang.status.spaceapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.fnordeingang.status.StatusState;
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
@JsonIgnoreProperties({
  "statusState",
  "handler",
  "targetClass",
  "targetInstance",
  "spaceApi"
})
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes("*/*")
@Path("/")
public class SpaceApi {
  @Inject
  private StatusState statusState;

  private String api = "0.13";
  private final String space = "fnordeingang";
  private String logo = "https://fnordeingang.de/wp-content/uploads/2013/06/logo_final21.png";
  private String url = "https://fnordeingang.de";
  private Location location = new Location("Körnerstr. 72, 41464 Neuss, Germany", 51.1862, 6.69232);
  private Contact contact = new Contact("@fnordeingang");
  @JsonProperty("issue_report_channels")
  private IssueReportChannels issueReportChannels = new IssueReportChannels("twitter");
  private State state;
  private boolean open;
  private Icon icon = new Icon(
      "https://fnordeingang.de/wp-content/uploads/2013/06/logo_final21.png",
      "https://fnordeingang.de/wp-content/uploads/2013/06/logo_final21.png"
  );

  @PostConstruct
  public void postConstruct() {
    state = new State(statusState.isOpen());
    open = statusState.isOpen();
  }

  @GET
  @Path("/spaceapi.json")
  public SpaceApi getSpaceApi() {
    return this;
  }
}
