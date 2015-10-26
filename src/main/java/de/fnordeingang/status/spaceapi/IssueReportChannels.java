package de.fnordeingang.status.spaceapi;

import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Value
public class IssueReportChannels {
	private final List<String> channels = new ArrayList<>();

	public IssueReportChannels(String ...channels) {
		Collections.addAll(this.channels, channels);
	}
}
