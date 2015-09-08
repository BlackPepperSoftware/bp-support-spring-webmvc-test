/*
 * Copyright 2014 Black Pepper Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
