package ru.practicum.shareit.bookingTests;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoJsonTest {
    @Autowired
    JacksonTester<BookingDto> jacksonTester;


    @Test
    void toJson() throws IOException {
        BookingDto dto = new BookingDto();
        dto.setId(1);
        dto.setStatus(Status.WAITING);
        dto.setStart(LocalDateTime.of(2023, 10, 10, 10, 10, 1));
        dto.setEnd(LocalDateTime.of(2023, 11, 11, 11, 11, 1));

        JsonContent<BookingDto> jsonContent = jacksonTester.write(dto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo(LocalDateTime.of(
                2023, 10, 10, 10, 10, 1).toString());
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo(LocalDateTime.of(
                2023, 11, 11, 11, 11, 1).toString());
    }

    @Test
    void toDto() throws IOException {
        String jsonBookingDto = "{\n" +
                "  \"id\": \"1\",\n" +
                "  \"status\": \"WAITING\",\n" +
                "  \"start\": \"2023-10-10T10:10:01\",\n" +
                "  \"end\": \"2023-11-11T11:11:01\"\n" +
                "}";
        BookingDto jsonContent = jacksonTester.parse(jsonBookingDto).getObject();
        AssertionsForClassTypes.assertThat(jsonContent.getId()).isEqualTo(1);
        AssertionsForClassTypes.assertThat(jsonContent.getStatus()).isEqualTo(Status.WAITING);
        AssertionsForClassTypes.assertThat(jsonContent.getStart()).isEqualTo(LocalDateTime.of(2023, 10, 10, 10, 10, 1));
        AssertionsForClassTypes.assertThat(jsonContent.getEnd()).isEqualTo(LocalDateTime.of(2023, 11, 11, 11, 11, 1));
    }

}