package ru.practicum.shareit.itemTests;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    JacksonTester<ItemDto> jacksonTester;


    @Test
    void toJson() throws IOException {
        ItemDto dto = new ItemDto();
        dto.setId(1);
        dto.setName("test");
        dto.setDescription("test");

        JsonContent<ItemDto> jsonContent = jacksonTester.write(dto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("test");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("test");
    }

    @Test
    void toDto() throws IOException {
        String jsonItemDto = "{\n" +
                "  \"id\": \"1\",\n" +
                "  \"name\": \"test\",\n" +
                "  \"description\": \"test\"\n" +
                "}";
        ItemDto jsonContent = jacksonTester.parse(jsonItemDto).getObject();
        AssertionsForClassTypes.assertThat(jsonContent.getId()).isEqualTo(1);
        AssertionsForClassTypes.assertThat(jsonContent.getName()).isEqualTo("test");
        AssertionsForClassTypes.assertThat(jsonContent.getDescription()).isEqualTo("test");
    }
}
