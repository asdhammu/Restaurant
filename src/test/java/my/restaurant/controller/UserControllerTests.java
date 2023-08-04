package my.restaurant.controller;

import my.restaurant.modal.forms.RegistrationForm;
import my.restaurant.repository.UserRepository;
import my.restaurant.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserDetailsService userDetailsService;
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserDetails userDetails;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmailId("test@test.com");
        registrationForm.setPassword("Test1234#");
        registrationForm.setFirstName("First");
        registrationForm.setLastName("Last");
        userService.save(registrationForm);
        userDetails = userDetailsService.loadUserByUsername("test@test.com");
    }


    @Test
    public void givenAccessSecuredResource_whenAuthenticated_thenRedirectedBack()
            throws Exception {

        MockHttpServletRequestBuilder securedResourceAccess = get("/user/addresses");
        MvcResult unauthenticatedResult = mockMvc
                .perform(securedResourceAccess)
                .andExpect(status().is3xxRedirection())
                .andReturn();

        MockHttpSession session = (MockHttpSession) unauthenticatedResult
                .getRequest()
                .getSession();
        String loginUrl = unauthenticatedResult
                .getResponse()
                .getRedirectedUrl();

        mockMvc.perform(post(loginUrl).param("username", userDetails.getUsername())
                        .param("password", "Test1234#")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection()).andReturn();

        mockMvc.perform(securedResourceAccess.session(session))
                .andExpect(status().isOk());

    }

}
