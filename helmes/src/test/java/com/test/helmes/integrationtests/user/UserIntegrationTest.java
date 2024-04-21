package com.test.helmes.integrationtests.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.helmes.HelmesApplication;
import com.test.helmes.controllers.user.UserController;
import com.test.helmes.dtos.user.UserDto;
import com.test.helmes.repositories.user.UserRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.annotations.Optional;

import javax.swing.text.html.Option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@AllArgsConstructor
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = HelmesApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class UserIntegrationTest {

    private UserRepository userRepository;
    private UserController userController;
    private MockMvc mockMvc;
    @BeforeEach
    void clearData() {
    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUserCreation() throws Exception {
        assertThat(userController).isNotNull();
        this.mockMvc.perform(
                MockMvcRequestBuilders
                .post("/user/register")
                .content(asJsonString(new UserDto("peeter", "supersecret", null)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated());
        assertThat(userRepository.getUserDboByUsername("peeter")).isPresent();
    }

    @Test
    void testUserLogin() throws Exception {

    }
}
