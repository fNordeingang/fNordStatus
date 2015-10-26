package de.fnordeingang.status.spaceapi;

import lombok.Value;

@Value
public class Location {
	private final String address;
	private final double lat;
	private final double lon;
}
