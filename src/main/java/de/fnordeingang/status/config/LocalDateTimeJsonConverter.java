package de.fnordeingang.status.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static de.fnordeingang.status.DateTimeHelper.getZonedDateTime;

public class LocalDateTimeJsonConverter extends JsonSerializer<LocalDateTime>
{
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

	@Override
	public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		ZonedDateTime zonedDateTime = getZonedDateTime(localDateTime);
		jsonGenerator.writeString(zonedDateTime.format(DATE_TIME_FORMATTER));
	}
}
