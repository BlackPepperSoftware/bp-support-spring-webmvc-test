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
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.CoreMatchers.is;

import uk.co.blackpepper.support.spring.webmvc.ModelAndViewDefiningException2;

public final class ModelAndViewDefiningException2Matcher extends TypeSafeMatcher<ModelAndViewDefiningException2> {

	private final Matcher<HttpStatus> statusMatcher;
	
	private final Matcher<ModelAndView> modelAndViewMatcher;
	
	private ModelAndViewDefiningException2Matcher(Matcher<HttpStatus> statusMatcher,
		Matcher<ModelAndView> modelAndViewMatcher) {
		this.statusMatcher = statusMatcher;
		this.modelAndViewMatcher = modelAndViewMatcher;
	}
	
	public static ModelAndViewDefiningException2Matcher modelAndViewDefiningException(HttpStatus expectedStatus) {
		return new ModelAndViewDefiningException2Matcher(is(expectedStatus), null);
	}

	public static ModelAndViewDefiningException2Matcher modelAndViewDefiningException(
		Matcher<ModelAndView> modelAndViewMatcher) {
		return new ModelAndViewDefiningException2Matcher(null, modelAndViewMatcher);
	}

	@Override
	public void describeTo(Description description) {
		if (statusMatcher != null) {
			description.appendText("Status: ");
			description.appendDescriptionOf(statusMatcher);
		}
		
		if (modelAndViewMatcher != null) {
			description.appendText("Model and view: ");
			description.appendDescriptionOf(modelAndViewMatcher);
		}
	}

	@Override
	protected boolean matchesSafely(ModelAndViewDefiningException2 actual) {
		return (statusMatcher == null || statusMatcher.matches(actual.getStatus()))
			&& (modelAndViewMatcher == null || modelAndViewMatcher.matches(actual.getModelAndView()));
	}

	public Matcher<HttpStatus> getStatusMatcher() {
		return statusMatcher;
	}

	public Matcher<ModelAndView> getModelAndViewMatcher() {
		return modelAndViewMatcher;
	}
}
