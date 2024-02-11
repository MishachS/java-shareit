package ru.practicum.shareit.itemTests;

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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemService itemService;

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
    void positiveAddItems() throws Exception {
        mockMvc.perform(
                post("/items")
                        .content("{\n" +
                                "    \"name\": \"Дрель\",\n" +
                                "    \"description\": \"Простая дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void negativeAddItems() throws Exception {
        mockMvc.perform(
                post("/items")
                        .content("{\n" +
                                "    \"name\": \"\",\n" +
                                "    \"description\": \"Простая дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void positiveUpdateItems() throws Exception {
        mockMvc.perform(
                post("/items")
                        .content("{\n" +
                                "    \"name\": \"Дрель\",\n" +
                                "    \"description\": \"Простая дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                patch("/items/1")
                        .content("{\n" +
                                "    \"name\": \"Дрельь\",\n" +
                                "    \"description\": \"Простаяя дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void negativeUpdateItems() throws Exception {
        mockMvc.perform(
                post("/items")
                        .content("{\n" +
                                "    \"name\": \"Дрель\",\n" +
                                "    \"description\": \"Простая дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                patch("/items/1")
                        .content("{\n" +
                                "    \"name\": \"Дрельь\",\n" +
                                "    \"description\": \"Простаяя дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void positiveGetItemsById() throws Exception {
        mockMvc.perform(
                post("/items")
                        .content("{\n" +
                                "    \"name\": \"Дрель\",\n" +
                                "    \"description\": \"Простая дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void negativeGetItemsById() throws Exception {
        mockMvc.perform(
                post("/items")
                        .content("{\n" +
                                "    \"name\": \"Дрель\",\n" +
                                "    \"description\": \"Простая дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/items/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void positiveGetAllItemsOneUser() throws Exception {
        mockMvc.perform(
                post("/items")
                        .content("{\n" +
                                "    \"name\": \"Дрель\",\n" +
                                "    \"description\": \"Простая дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    void positiveSearchItemByText() throws Exception {
        mockMvc.perform(
                post("/items")
                        .content("{\n" +
                                "    \"name\": \"Дрель\",\n" +
                                "    \"description\": \"Простая дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/items/search?text='дрель'")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void negativeSearchItemByText() throws Exception {
        mockMvc.perform(
                post("/items")
                        .content("{\n" +
                                "    \"name\": \"Дрель\",\n" +
                                "    \"description\": \"Простая дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void positiveAddComment() throws Exception {
        mockMvc.perform(
                post("/items")
                        .content("{\n" +
                                "    \"name\": \"Дрель\",\n" +
                                "    \"description\": \"Простая дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "    \"name\": \"user2\",\n" +
                                "    \"email\": \"user2@user.com\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        LocalDateTime now = LocalDateTime.now().plusSeconds(1);
        String start = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        LocalDateTime now1 = LocalDateTime.now().plusSeconds(2);
        String start1 = now1.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        mockMvc.perform(
                post("/bookings")
                        .content("{\n" +
                                "    \"itemId\": 1,\n" +
                                "    \"start\": \"" + start + "\",\n" +
                                "    \"end\": \"" + start1 + "\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
        ).andExpect(status().isOk());


        mockMvc.perform(
                patch("/bookings/1?approved=true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());


        Optional<Booking> booking = bookingRepository.findById(1);
        booking.get().setStart(LocalDateTime.now());
        bookingRepository.deleteById(1);
        bookingRepository.save(booking.get());


        mockMvc.perform(
                post("/items/1/comment")
                        .content("{\n" +
                                "    \"text\": \"Comment for item 1\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
        ).andExpect(status().is2xxSuccessful());

        mockMvc.perform(
                patch("/items/1")
                        .content("{\n" +
                                "    \"name\": \"Дрельь\",\n" +
                                "    \"description\": \"Простаяя дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void negativeAddComment() throws Exception {
        mockMvc.perform(
                post("/items")
                        .content("{\n" +
                                "    \"name\": \"Дрель\",\n" +
                                "    \"description\": \"Простая дрель\",\n" +
                                "    \"available\": true\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
        ).andExpect(status().isOk());

        mockMvc.perform(
                post("/items/1/comment")
                        .content("{\n" +
                                "    \"text\": \"Comment for item 1\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
        ).andExpect(status().is4xxClientError());
    }
}
