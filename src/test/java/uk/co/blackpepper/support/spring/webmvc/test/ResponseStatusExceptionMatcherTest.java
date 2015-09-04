package uk.co.blackpepper.support.spring.webmvc.test;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import uk.co.blackpepper.support.spring.webmvc.ResponseStatusException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import static uk.co.blackpepper.support.spring.webmvc.test.ResponseStatusExceptionMatcher.responseStatusException;

public class ResponseStatusExceptionMatcherTest {

	@Test
	public void responseStatusExceptionReturnsMatcher() {
		ResponseStatusExceptionMatcher actual = responseStatusException(HttpStatus.OK);

		assertThat("statusMatcher", actual.getStatusMatcher().matches(HttpStatus.OK), is(true));
	}
	
	@Test
	public void describeToAppendsDescription() {
		StringDescription description = new StringDescription();
		
		responseStatusException(HttpStatus.OK).describeTo(description);
		
		assertThat(description.toString(), is("Status: is <200>"));
	}

	@Test
	public void matchesSafelyWithMatchingStatusReturnsTrue() {
		ResponseStatusException exception = new ResponseStatusException(HttpStatus.OK);
		
		boolean actual = responseStatusException(HttpStatus.OK).matchesSafely(exception);
		
		assertThat(actual, is(true));
	}

	@Test
	public void matchesSafelyWithNonMatchingStatusReturnsFalse() {
		ResponseStatusException exception = new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		boolean actual = responseStatusException(HttpStatus.OK).matchesSafely(exception);
		
		assertThat(actual, is(false));
	}
}
