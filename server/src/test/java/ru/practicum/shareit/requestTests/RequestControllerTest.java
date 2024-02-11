package ru.practicum.shareit.requestTests;

import org.junit.jupiter.api.BeforeEach;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void preset() throws Exception {
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
    void positiveAddRequest() throws Exception {
        mockMvc.perform(
                post("/requests")
                        .content("{\n" +
                                "    \"description\": \"Хотел бы воспользоваться щёткой для обуви\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

    }

    @Test
    void negativeAddRequest() throws Exception {
        mockMvc.perform(
                post("/requests")
                        .content("{\n" +
                                "    \"description\": \"Хотел бы воспользоваться щёткой для обуви\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void positiveGetAllRequestOneUser() throws Exception {
        mockMvc.perform(
                post("/requests")
                        .content("{\n" +
                                "    \"description\": \"Хотел бы воспользоваться щёткой для обуви\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void negativeGetAllRequestOneUser() throws Exception {
        mockMvc.perform(
                post("/requests")
                        .content("{\n" +
                                "    \"description\": \"Хотел бы воспользоваться щёткой для обуви\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void positiveGetRequestsAllUsers() throws Exception {
        mockMvc.perform(
                get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    void negativeGetRequestsAllUsers() throws Exception {
        mockMvc.perform(
                get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void positiveGetRequestById() throws Exception {
        mockMvc.perform(
                post("/requests")
                        .content("{\n" +
                                "    \"description\": \"Хотел бы воспользоваться щёткой для обуви\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    void negativeGetRequestById() throws Exception {
        mockMvc.perform(
                post("/requests")
                        .content("{\n" +
                                "    \"description\": \"Хотел бы воспользоваться щёткой для обуви\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
        ).andExpect(status().is4xxClientError());
    }
}
