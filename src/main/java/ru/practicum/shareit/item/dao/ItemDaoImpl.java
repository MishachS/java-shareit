package ru.practicum.shareit.item.dao;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
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
        if (!Objects.equals(originalItem.getOwner().getId(), item.getOwner().getId())) {
            throw new AccessDeniedException("У вас нет прав для редактирования чужого объявления!");
        }
        Optional.ofNullable(item.getName()).ifPresent(originalItem::setName);
        Optional.ofNullable(item.getDescription()).ifPresent(originalItem::setDescription);
        Optional.ofNullable(item.getAvailable()).ifPresent(originalItem::setAvailable);
        return itemRepository.save(originalItem);
    }

    @Transactional
    @Override
    public Item getItemById(int id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь по вашему id = " + id + " не найдена!"));
    }

    @Override
    @Transactional
    public List<Item> searchItemByText(String text, int from, int size) {
        return itemRepository.findAll(PageRequest.of(from, size))
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

    @Override
    public List<Item> getAllItemsByOneRequest(int requestId) {
        return itemRepository.findAllByRequestId(requestId);
    }

    @Override
    public Page<Item> findAllByOwnerId(Integer ownerId, Pageable pageable) {
        return itemRepository.findAllByOwnerId(ownerId, pageable);
    }

    @Override
    public List<Item> findAllByRequestIdIn(List<Integer> requestIds) {
        return itemRepository.findAllByRequestIdIn(requestIds);
    }

    private boolean containsText(Item item, String text) {
        return item.getName().toLowerCase().contains(text.toLowerCase()) ||
                item.getDescription().toLowerCase().contains(text.toLowerCase());
    }
}
