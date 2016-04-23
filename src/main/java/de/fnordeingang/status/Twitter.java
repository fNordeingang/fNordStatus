package de.fnordeingang.status;

import de.fnordeingang.status.config.LocalDateTimeJsonConverter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.util.logging.Logger;

import static de.fnordeingang.status.DateTimeHelper.getZonedDateTime;

@ApplicationScoped
public class Twitter {
	@Inject
	private Logger logger;

	@Inject
	private StatusState statusState;

	private twitter4j.Twitter twitter = TwitterFactory.getSingleton();

	void tweetStatus() {
		try {
			String text = String.format(
					"#fnordeingang %s %s",
					statusState.isOpen() ? "is now open!" : "has closed, see you soon!",
					getZonedDateTime(statusState.getChangedAt())
							.format(LocalDateTimeJsonConverter.DATE_TIME_FORMATTER));
			twitter.updateStatus(text);
		} catch (TwitterException | IllegalStateException e) {
			logger.info(e.getMessage());
			throw new InternalServerErrorException(e.getMessage());
		}
	}
}
