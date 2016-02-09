package org.learn.self.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.learn.self.ConfigClientApplication;
import org.learn.self.service.GreetingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConfigClientApplication.class)
public class GreetingControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private GreetingProvider provider;

    @Autowired
    private GreetingController controller;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGreeter() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(provider.getGreeting() + " World!"));
    }
}
