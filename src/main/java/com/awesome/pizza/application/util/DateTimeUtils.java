package com.awesome.pizza.application.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class DateTimeUtils {

	private DateTimeUtils() {
	} // DateTimeUtils

	public static long toEpochMillis(LocalDateTime ldt) {
		return ldt.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
	} // toEpochMillis

	public static LocalDateTime fromEpochMillis(long millis) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.systemDefault());
	} // fromEpochMillis
} // DateTimeUtils
