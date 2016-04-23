package de.fnordeingang.status.spaceapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IssueReportChannels extends ArrayList<String> {
	IssueReportChannels(String... channels) {
		Collections.addAll(this, channels);
	}
}
