package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class Item {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer owner;
    private Integer request;
}