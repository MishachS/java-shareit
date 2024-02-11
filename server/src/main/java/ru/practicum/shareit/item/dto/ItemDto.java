package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoInput;

import java.util.List;

@Data
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
    private List<CommentDto> comments;
    private BookingDtoInput lastBooking;
    private BookingDtoInput nextBooking;
}
