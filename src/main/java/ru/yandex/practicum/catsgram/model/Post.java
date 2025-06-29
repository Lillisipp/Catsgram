package ru.yandex.practicum.catsgram.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {
    private Long id;
    private long authorId;
    private String description;
    private Instant postDate;

}
