FROM jboss/wildfly

ADD src/main/resources/twitter4j.properties /opt/jboss/wildfly/standalone/deployments/twitter4j.properties
ADD target/fnord-status.war /opt/jboss/wildfly/standalone/deployments/fnord-status.war
