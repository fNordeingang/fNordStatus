package de.fnordeingang.status.response;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class StatusResponse {
	private final boolean isOpen;
	private final LocalDateTime changedAt;
}
