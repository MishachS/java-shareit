package ru.practicum.shareit.itemTests;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentDtoJsonTest {
    @Autowired
    JacksonTester<CommentDto> jacksonTester;


    @Test
    void toJson() throws IOException {
        CommentDto dto = new CommentDto();
        dto.setId(1);
        dto.setText("test");
        dto.setAuthorName("test");
        dto.setCreated(LocalDateTime.of(2023, 11, 11, 11, 11, 1));

        JsonContent<CommentDto> jsonContent = jacksonTester.write(dto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.text").isEqualTo("test");
        assertThat(jsonContent).extractingJsonPathStringValue("$.authorName").isEqualTo("test");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isEqualTo(
                LocalDateTime.of(2023, 11, 11, 11, 11, 1).toString());
    }

    @Test
    void toDto() throws IOException {
        String jsonItemDto = "{\n" +
                "  \"id\": \"1\",\n" +
                "  \"text\": \"test\",\n" +
                "  \"authorName\": \"test\",\n" +
                "  \"created\": \"2023-11-11T11:11:01\"\n" +
                "}";
        CommentDto jsonContent = jacksonTester.parse(jsonItemDto).getObject();
        AssertionsForClassTypes.assertThat(jsonContent.getId()).isEqualTo(1);
        AssertionsForClassTypes.assertThat(jsonContent.getText()).isEqualTo("test");
        AssertionsForClassTypes.assertThat(jsonContent.getAuthorName()).isEqualTo("test");
        AssertionsForClassTypes.assertThat(jsonContent.getCreated()).isEqualTo(LocalDateTime.of(2023, 11, 11, 11, 11, 1));
    }
}
