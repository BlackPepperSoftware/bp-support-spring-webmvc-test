package uk.co.blackpepper.support.spring.webmvc.test;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.http.HttpStatus;

import uk.co.blackpepper.support.spring.webmvc.ResponseStatusException;

import static org.hamcrest.CoreMatchers.is;

public final class ResponseStatusExceptionMatcher extends TypeSafeMatcher<ResponseStatusException> {

	private final Matcher<HttpStatus> statusMatcher;
	
	private ResponseStatusExceptionMatcher(Matcher<HttpStatus> statusMatcher) {
		this.statusMatcher = statusMatcher;
	}

	public static ResponseStatusExceptionMatcher responseStatusException(HttpStatus expectedStatus) {
		return new ResponseStatusExceptionMatcher(is(expectedStatus));
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("Status: ");
		description.appendDescriptionOf(statusMatcher);
	}

	@Override
	protected boolean matchesSafely(ResponseStatusException item) {
		return statusMatcher.matches(item.getStatus());
	}
	
	public Matcher<HttpStatus> getStatusMatcher() {
		return statusMatcher;
	}
}
