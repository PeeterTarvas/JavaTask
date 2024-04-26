package com.test.helmes.integrationtests.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.helmes.dtos.user.UserDto;
import com.test.helmes.repositories.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

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
    @Order(1)
    void testUserCreation() throws Exception {
        UserDto user = new UserDto("peeter", "supersecret", null);
        this.mockMvc.perform(
                MockMvcRequestBuilders
                .post("/user/register")
                .content(asJsonString(user))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated());
        assertThat(userRepository.getUserDboByUsername("peeter")).isPresent();
    }

    @Test
    @Order(2)
    void testUserLogin() throws Exception {
        UserDto user = new UserDto("peeter", "supersecret", null);
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/user/register")
                        .content(asJsonString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
        assertThat(userRepository.getUserDboByUsername("peeter")).isPresent();
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/user/login")
                        .content(asJsonString(new UserDto("peeter", "supersecret", null)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

    }
}
