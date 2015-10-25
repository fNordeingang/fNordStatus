package de.fnordeingang.status.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

	final ObjectMapper mapper = new ObjectMapper();

	public ObjectMapperContextResolver() {
		SimpleModule module = new SimpleModule();
		module.addSerializer(LocalDateTime.class, new LocalDateTimeJsonConverter());
		mapper.registerModule(module);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}
}