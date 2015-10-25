package de.fnordeingang.status;

import de.fnordeingang.status.config.LocalDateTimeJsonConverter;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Logger;

import static de.fnordeingang.status.DateTimeHelper.getZonedDateTime;

@ApplicationScoped
public class Twitter {
	@Inject
	private Logger logger;

	@Inject
	private Status status;

	twitter4j.Twitter twitter = TwitterFactory.getSingleton();

	public void getTimeline() throws TwitterException {
		ResponseList<twitter4j.Status> statuses = twitter.getHomeTimeline();
		logger.info("Showing home timeline.");
		for (twitter4j.Status status : statuses) {
			logger.info(String.format("%s:%s", status.getUser().getName(), status.getText()));
		}
	}

	public void tweet() {
		try {
			String text = String.format(
					"#fnordeingang %s %s",
					status.isOpen() ? "is now open!" : "has closed, see you soon!",
					getZonedDateTime(status.getChangedAt())
							.format(LocalDateTimeJsonConverter.DATE_TIME_FORMATTER));
			twitter.updateStatus(text);
		} catch (TwitterException e) {
			logger.info(e.getMessage());
		}
	}
}
