package de.fnordeingang.status.config;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

@Stateless
public class LoggerProducer {
	@Produces
	public Logger getLogger(InjectionPoint ip) {
		return java.util.logging.Logger.getLogger(ip.getMember().getDeclaringClass().getSimpleName());
	}
}
