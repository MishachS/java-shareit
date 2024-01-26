package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}
