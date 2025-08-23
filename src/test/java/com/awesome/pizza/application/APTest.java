package com.awesome.pizza.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.function.Executable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.awesome.pizza.application.data.exception.APException;

public abstract class APTest {

	protected <E extends APException> void assertAPException(Class<E> exClass, Executable executable) {
		assertAPException(exClass, executable, null);
	} // assertAPException

	protected <E extends APException> void assertAPException(Class<E> exClass, Executable executable,
			String expectedTxt) {
		String name = exClass.getSimpleName();
		// If no errorMessage was provided, it tries to retrieve the one set as
		// `ResponseStatus.reason`
		if (expectedTxt == null) {
			// If a 'reason' was set, then it uses it to assert the error message
			ResponseStatus annotation = exClass.getAnnotation(ResponseStatus.class);
			if (annotation != null && StringUtils.hasLength(annotation.reason())) {
				expectedTxt = annotation.reason();
			}
		}

		E exc = assertThrowsExactly(exClass, executable, "Expected '" + name + "'");
		if (expectedTxt != null) {
			assertEquals(exc.getLocalizedMessage(), expectedTxt);
		}
	} // assertAPException
} // APTest
