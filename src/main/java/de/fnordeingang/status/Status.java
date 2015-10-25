package de.fnordeingang.status;

import lombok.Data;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Data
@Singleton
public class Status {
	public static final int CLOSE_AFTER_MINUTES = 2;
	@Inject
	private Twitter twitter;

	@Inject
	private Logger logger;

	private boolean isOpen = false;
	private LocalDateTime changedAt = LocalDateTime.now();
	private LocalDateTime lastPing = LocalDateTime.now();

	public void open() {
		setOpen(true);
	}

	public void close() {
		setOpen(false);
	}

	public void setOpen(boolean status) {
		boolean lastStatus = isOpen;
		isOpen = status;
		changedAt = LocalDateTime.now();

		if(status != lastStatus) {
			twitter.tweet();
		}

		logger.info(String.format("status set to %s at %s with lastPing %s",
				isOpen,
				changedAt.format(DateTimeFormatter.ISO_DATE_TIME),
				lastPing.format(DateTimeFormatter.ISO_DATE_TIME)));
	}

	@Schedule(second = "59", minute = "*", hour = "*", persistent = false)
	public void timer() {
		Duration duration = Duration.between(lastPing, LocalDateTime.now());
		if(duration.getSeconds() >= 60*CLOSE_AFTER_MINUTES && isOpen) {
			logger.info("closing fnord due to timeout");
			close();
		}

		logger.info(String.format("no timeout happened, keeping current status %s", isOpen));
	}
}
