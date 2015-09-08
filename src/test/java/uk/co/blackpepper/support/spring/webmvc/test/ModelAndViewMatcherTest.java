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
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import static uk.co.blackpepper.support.spring.webmvc.test.ModelAndViewMatcher.modelAndView;

public class ModelAndViewMatcherTest {

	@Test
	public void modelAndViewWithViewNameReturnsMatcher() {
		ModelAndViewMatcher actual = modelAndView("x");
		
		assertThat("viewNameMatcher", actual.getViewNameMatcher().matches("x"), is(true));
		assertThat("modelMatcher", actual.getModelMatcher(), is(nullValue()));
	}

	@Test
	public void modelAndViewWithModelReturnsMatcher() {
		Matcher<? extends Map<? extends String, ? extends Object>> modelMatcher = mock(Matcher.class);
		
		ModelAndViewMatcher actual = modelAndView(modelMatcher);
		
		assertThat("viewNameMatcher", actual.getViewNameMatcher(), is(nullValue()));
		assertThat("modelMatcher", actual.getModelMatcher(), is((Object) modelMatcher));
	}
	
	@Test
	public void describeToWhenViewNameAppendsDescription() {
		StringDescription description = new StringDescription();
		
		modelAndView("x").describeTo(description);
		
		assertThat(description.toString(), is("View name: is \"x\""));
	}

	@Test
	public void describeToWhenModelAppendsDescription() {
		Matcher<? extends Map<? extends String, ? extends Object>> modelMatcher = mock(Matcher.class);
		doAnswer(appendText("x")).when(modelMatcher).describeTo(any(Description.class));
		StringDescription description = new StringDescription();
		
		modelAndView(modelMatcher).describeTo(description);
		
		assertThat(description.toString(), is("Model: x"));
	}

	@Test
	public void matchesSafelyWhenViewNameWithMatchingViewNameReturnsTrue() {
		ModelAndViewMatcher matcher = modelAndView("x");
		
		boolean actual = matcher.matchesSafely(new ModelAndView("x"));
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void matchesSafelyWhenViewNameWithNonMatchingViewNameReturnsFalse() {
		ModelAndViewMatcher matcher = modelAndView("x");
		
		boolean actual = matcher.matchesSafely(new ModelAndView("y"));
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void matchesSafelyWhenModelWithMatchingModelReturnsTrue() {
		ModelAndViewMatcher matcher = modelAndView(hasEntry("x", "y"));
		
		boolean actual = matcher.matchesSafely(new ModelAndView("", "x", "y"));
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void matchesSafelyWhenModelWithNonMatchingModelReturnsFalse() {
		ModelAndViewMatcher matcher = modelAndView(hasEntry("x", "y"));
		
		boolean actual = matcher.matchesSafely(new ModelAndView("", "x", "z"));
		
		assertThat(actual, is(false));
	}
	
	private static Answer<Object> appendText(final String text) {
		return new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) {
				Description description = invocation.getArgumentAt(0, Description.class);
				description.appendText(text);
				return null;
			}
		};
	}
}
