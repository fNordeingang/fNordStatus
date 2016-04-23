package de.fnordeingang.status;

import de.fnordeingang.status.config.*;
import de.fnordeingang.status.response.StatusResponse;
import de.fnordeingang.status.rest.StatusResource;
import de.fnordeingang.status.spaceapi.*;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;


public class StatusMain {
  public static void main(String[] args) throws Exception {
    Container container = new Container();

    JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "fnord-status.war");
    deployment.addClass(JAXRSConfiguration.class);
    deployment.addClass(CORSFilter.class);
    deployment.addClass(LocalDateTimeJsonConverter.class);
    deployment.addClass(LoggerProducer.class);
    deployment.addClass(DateTimeHelper.class);
    deployment.addClass(ObjectMapperContextResolver.class);
    deployment.addClass(StatusResource.class);
    deployment.addClass(StatusResponse.class);
    deployment.addClass(Icon.class);
    deployment.addClass(StatusState.class);
    deployment.addClass(Twitter.class);
    deployment.addClass(SpaceApi.class);
    deployment.addClass(Location.class);
    deployment.addClass(State.class);
    deployment.addClass(Contact.class);
    deployment.addClass(IssueReportChannels.class);
    deployment.addAllDependencies();
    container.start().deploy(deployment);
  }
}
