package ru.yandex.practicum.catsgram.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Image {
    private Long id;
    private long postId;
    private String originalFileName;
    private String filePath;

}
