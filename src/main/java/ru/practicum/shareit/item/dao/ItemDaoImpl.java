package ru.practicum.shareit.item.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ItemDaoImpl implements ItemDao {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item updateItem(int itemId, Item item) {
        Item originalItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь по вашему id = " + itemId + " не найдена!"));
        if (originalItem.getOwner().getId() != item.getOwner().getId()) {
            throw new OwnerException("У вас нет прав для редактирования чужого объявления!");
        }
        Optional.ofNullable(item.getName()).ifPresent(originalItem::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(originalItem::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(originalItem::setAvailable);
        return itemRepository.save(originalItem);
    }

    @Override
    @Transactional
    public Item getItemById(int id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь по вашему id = " + id + " не найдена!"));
    }

    @Override
    @Transactional
    public List<Item> getAllItemsForUser(int ownerId) {
        return itemRepository.findAll()
                .stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Item> searchItemByText(String text) {
        return itemRepository.findAll()
                .stream()
                .filter(Item::getAvailable)
                .filter(item -> containsText(item, text))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Comment addComment(Comment comment) {
        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public List<Comment> getAllCommentOneItem(int id) {
        return commentRepository.findByItemId(id);
    }

    private boolean containsText(Item item, String text) {
        return item.getName().toLowerCase().contains(text.toLowerCase()) ||
                item.getDescription().toLowerCase().contains(text.toLowerCase());
    }
}
