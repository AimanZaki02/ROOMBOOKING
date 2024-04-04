package com.library.room;

import javax.persistence.*;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(length = 100, nullable = false)
    private String location;

    @Column(length = 10, nullable = false)
    private Integer capacity;

    // Getters and Setters
}
