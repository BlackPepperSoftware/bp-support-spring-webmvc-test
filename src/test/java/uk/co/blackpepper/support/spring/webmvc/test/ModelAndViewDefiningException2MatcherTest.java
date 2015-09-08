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
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import uk.co.blackpepper.support.spring.webmvc.ModelAndViewDefiningException2;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import static uk.co.blackpepper.support.spring.webmvc.test.ModelAndViewDefiningException2Matcher.modelAndViewDefiningException;

public class ModelAndViewDefiningException2MatcherTest {

	@Test
	public void modelAndViewDefiningExceptionWithStatusReturnsMatcher() {
		ModelAndViewDefiningException2Matcher actual = modelAndViewDefiningException(HttpStatus.OK);
		
		assertThat("statusMatcher", actual.getStatusMatcher().matches(HttpStatus.OK), is(true));
		assertThat("modelAndViewMatcher", actual.getModelAndViewMatcher(), is(nullValue()));
	}

	@Test
	public void modelAndViewDefiningExceptionWithModelAndViewReturnsMatcher() {
		Matcher<ModelAndView> modelAndViewMatcher = mock(Matcher.class);
		
		ModelAndViewDefiningException2Matcher actual = modelAndViewDefiningException(modelAndViewMatcher);
		
		assertThat("statusMatcher", actual.getStatusMatcher(), is(nullValue()));
		assertThat("modelAndViewMatcher", actual.getModelAndViewMatcher(), is((Object) modelAndViewMatcher));
	}
	
	@Test
	public void describeToWhenStatusAppendsDescription() {
		StringDescription description = new StringDescription();
		
		modelAndViewDefiningException(HttpStatus.OK).describeTo(description);
		
		assertThat(description.toString(), is("Status: is <200>"));
	}

	@Test
	public void describeToWhenModelAndViewAppendsDescription() {
		Matcher<ModelAndView> modelAndViewMatcher = mock(Matcher.class);
		doAnswer(appendText("x")).when(modelAndViewMatcher).describeTo(any(Description.class));
		StringDescription description = new StringDescription();
		
		modelAndViewDefiningException(modelAndViewMatcher).describeTo(description);
		
		assertThat(description.toString(), is("Model and view: x"));
	}
	
	@Test
	public void matchesSafelyWhenStatusWithMatchingStatusReturnsTrue() {
		ModelAndViewDefiningException2 exception = new ModelAndViewDefiningException2(new ModelAndView(),
			HttpStatus.OK);
		
		boolean actual = modelAndViewDefiningException(HttpStatus.OK).matchesSafely(exception);
		
		assertThat(actual, is(true));
	}

	@Test
	public void matchesSafelyWhenStatusWithNonMatchingStatusReturnsFalse() {
		ModelAndViewDefiningException2 exception = new ModelAndViewDefiningException2(new ModelAndView(),
			HttpStatus.NOT_FOUND);
		
		boolean actual = modelAndViewDefiningException(HttpStatus.OK).matchesSafely(exception);
		
		assertThat(actual, is(false));
	}

	@Test
	public void matchesSafelyWhenModelAndViewWithMatchingModelAndViewReturnsTrue() {
		ModelAndView modelAndView = new ModelAndView();
		ModelAndViewDefiningException2 exception = new ModelAndViewDefiningException2(modelAndView);
		
		boolean actual = modelAndViewDefiningException(is(modelAndView)).matchesSafely(exception);
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void matchesSafelyWhenModelAndViewWithNonMatchingModelAndViewReturnsFalse() {
		ModelAndViewDefiningException2 exception = new ModelAndViewDefiningException2(new ModelAndView());
		
		boolean actual = modelAndViewDefiningException(is(new ModelAndView())).matchesSafely(exception);
		
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
