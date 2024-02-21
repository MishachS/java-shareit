package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    public int id;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(nullable = false)
    public LocalDateTime created;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "requester_id")
    public User requester;
}