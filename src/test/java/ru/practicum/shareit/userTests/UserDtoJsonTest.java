package ru.practicum.shareit.userTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserDtoJsonTest {
    @Autowired
    JacksonTester<UserDto> jacksonTester;

    @Test
    void toJson() throws Exception {
        UserDto dto = new UserDto();
        dto.setId(1);
        dto.setName("vovan");
        dto.setEmail("vovan1479@mail.com");

        JsonContent<UserDto> jsonContent = jacksonTester.write(dto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("vovan");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("vovan1479@mail.com");
    }

    @Test
    void toDto() throws IOException {
        String json = "{\n" +
                "  \"id\": \"1\",\n" +
                "  \"name\": \"vovan\",\n" +
                "  \"email\": \"vovan1479@mail.com\"\n" +
                "}";
        UserDto jsonContent = jacksonTester.parse(json).getObject();
        assertThat(jsonContent.getId()).isEqualTo(1);
        assertThat(jsonContent.getName()).isEqualTo("vovan");
        assertThat(jsonContent.getEmail()).isEqualTo("vovan1479@mail.com");
    }

}