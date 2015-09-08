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

import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.CoreMatchers.is;

public final class ModelAndViewMatcher extends TypeSafeMatcher<ModelAndView> {

	private final Matcher<String> viewNameMatcher;
	
	private final Matcher<? extends Map<? extends String, ? extends Object>> modelMatcher;
	
	private ModelAndViewMatcher(Matcher<String> viewNameMatcher,
		Matcher<? extends Map<? extends String, ? extends Object>> modelMatcher) {
		this.viewNameMatcher = viewNameMatcher;
		this.modelMatcher = modelMatcher;
	}
	
	public static ModelAndViewMatcher modelAndView(String expectedViewName) {
		return new ModelAndViewMatcher(is(expectedViewName), null);
	}
	
	public static ModelAndViewMatcher modelAndView(
		Matcher<? extends Map<? extends String, ? extends Object>> modelMatcher) {
		return new ModelAndViewMatcher(null, modelMatcher);
	}
	
	@Override
	public void describeTo(Description description) {
		if (viewNameMatcher != null) {
			description.appendText("View name: ");
			description.appendDescriptionOf(viewNameMatcher);
		}
		
		if (modelMatcher != null) {
			description.appendText("Model: ");
			description.appendDescriptionOf(modelMatcher);
		}
	}
	
	@Override
	protected boolean matchesSafely(ModelAndView actual) {
		return (viewNameMatcher == null || viewNameMatcher.matches(actual.getViewName()))
			&& (modelMatcher == null || modelMatcher.matches(actual.getModel()));
	}

	public Matcher<String> getViewNameMatcher() {
		return viewNameMatcher;
	}

	public Matcher<? extends Map<? extends String, ? extends Object>> getModelMatcher() {
		return modelMatcher;
	}
}
