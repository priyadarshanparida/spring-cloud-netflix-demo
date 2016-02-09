package org.learn.self.controller;

import org.junit.Rule;
import org.junit.Test;
import org.learn.self.service.GreetingProvider;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockitoGreetingControllerTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private GreetingProvider provider;

    @InjectMocks
    private GreetingController controller;

    @Test
    public void testGreeting() throws Exception {
        when(provider.getGreeting()).thenReturn("Hello");
        assertThat(controller.greeter(), equalTo("Hello World!"));
    }

    @Test
    public void testGreeting2() throws Exception {
        controller.greeter();

        verify(provider).getGreeting();
    }
}
