package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping // addItems Добавление новой вещи
    public ResponseEntity<Object> addItems(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return itemClient.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItems(@PathVariable Integer itemId, @RequestBody ItemDto itemDto,
                                              @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return itemClient.updateItem(itemId, itemDto, ownerId);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemsById(@PathVariable Integer itemId, @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return itemClient.getItemById(itemId, ownerId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllItemsOneUser(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                                     @Positive @RequestParam(defaultValue = "20", required = false) Integer size) {
        return itemClient.getAllItemsOneUserOrderByIdAsc(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemByText(@RequestParam String text,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                                   @Positive @RequestParam(defaultValue = "20", required = false) Integer size) {
        return itemClient.searchItemByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Integer itemId, @RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @Valid @RequestBody CommentDto commentDto) {
        return itemClient.addComment(itemId, userId, commentDto);
    }
}