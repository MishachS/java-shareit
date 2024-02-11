package ru.practicum.shareit.userTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void positiveAddUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "    \"name\": \"user\",\n" +
                                "    \"email\": \"user@user.com\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void negativeAddUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "    \"name\": \"user\",\n" +
                                "    \"email\": \"user\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void positiveUpdateUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "    \"name\": \"user\",\n" +
                                "    \"email\": \"user@user.com\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        mockMvc.perform(
                patch("/users/1")
                        .content("{\n" +
                                "    \"name\": \"user1\",\n" +
                                "    \"email\": \"user1@user.com\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void negativeUpdateUser() throws Exception {
        mockMvc.perform(
                patch("/users/1")
                        .content("{\n" +
                                "    \"name\": \"user1\",\n" +
                                "    \"email\": \"user1@user.com\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void positiveGetUserById() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "    \"name\": \"user\",\n" +
                                "    \"email\": \"user@user.com\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void negativeGetUserById() throws Exception {
        mockMvc.perform(
                get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void positiveGetAllUser() throws Exception {
        mockMvc.perform(
                get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void positiveDeleteUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "    \"name\": \"user\",\n" +
                                "    \"email\": \"user@user.com\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        mockMvc.perform(
                delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void negativeDeleteUser() throws Exception {
        mockMvc.perform(
                delete("/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }
}
