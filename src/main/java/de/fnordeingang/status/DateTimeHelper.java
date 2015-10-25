package de.fnordeingang.status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeHelper {
	public static ZonedDateTime getZonedDateTime(LocalDateTime localDateTime) {
		ZoneId timeZone = ZoneId.systemDefault();
		return localDateTime.atZone(timeZone).withZoneSameInstant(ZoneId.of("UTC"));
	}
}
