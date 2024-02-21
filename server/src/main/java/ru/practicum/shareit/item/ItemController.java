package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private ItemService itemService;

    @PostMapping
    public ItemDto addItems(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("Метод addItems . userId " + ownerId);
        return itemService.addItems(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItems(@PathVariable Integer itemId, @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("Метод updateItems . userId " + ownerId + " itemId " + itemId);
        return itemService.updateItems(itemId, itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemsById(@PathVariable Integer itemId, @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("Метод getItemsById . userId " + ownerId + " itemId " + itemId);
        return itemService.getItemsById(itemId, ownerId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsOneUser(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                            @Positive @RequestParam(defaultValue = "20", required = false) Integer size) {
        log.info("Метод getAllItemsOneUser . userId " + ownerId);
        return itemService.getAllItemsOneUser(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam String text,
                                          @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                          @Positive @RequestParam(defaultValue = "20", required = false) Integer size) {
        log.info("Метод searchItemByText");
        return itemService.searchItemByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Integer itemId, @RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("Метод addComment . userId " + userId + " itemId " + itemId);
        return itemService.addComment(itemId, userId, commentDto);
    }
}