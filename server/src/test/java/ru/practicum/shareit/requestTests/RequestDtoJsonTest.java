package ru.practicum.shareit.requestTests;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestDtoJsonTest {
    @Autowired
    JacksonTester<ItemRequestDto> jacksonTester;

    @Test
    void toJson() throws IOException {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(1);
        dto.setDescription("test");
        dto.setCreated(LocalDateTime.of(2023, 11, 11, 11, 11, 1));

        JsonContent<ItemRequestDto> jsonContent = jacksonTester.write(dto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isEqualTo(
                LocalDateTime.of(2023, 11, 11, 11, 11, 1).toString());

    }

    @Test
    void toDto() throws IOException {
        String json = "{\n" +
                "  \"id\": \"1\",\n" +
                "  \"description\": \"test\",\n" +
                "  \"created\": \"2023-11-11T11:11:01\"\n" +
                "}";
        ItemRequestDto jsonContent = jacksonTester.parse(json).getObject();
        AssertionsForClassTypes.assertThat(jsonContent.getId()).isEqualTo(1);
        AssertionsForClassTypes.assertThat(jsonContent.getDescription()).isEqualTo("test");
        AssertionsForClassTypes.assertThat(jsonContent.getCreated()).isEqualTo(LocalDateTime.of(2023, 11, 11, 11, 11, 1));
    }

}
