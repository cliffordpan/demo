package me.hchome.example.dto;

import java.time.Instant;

/**
 * @author Cliff Pan
 * @since
 */
public record ErrorEntity(
	String message,
	String code,
	Instant timestamp
) {}