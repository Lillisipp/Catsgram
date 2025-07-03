package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.model.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.yandex.practicum.catsgram.utils.Utils.getNextId;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final PostService postService;
    private final Map<Long, Image> images = new HashMap<>();
    private final String imageDirectory = "images";//"C:\....";

    public List<Image> getPostImages(Long postID) {
        return images.values()
                .stream()
                .filter(image -> image.getPostId() == postID)
                .collect(Collectors.toList());
    }

    public List<Image> seveImages(Long postID, List<MultipartFile> files) {
        return images.values()
                .stream()
                .filter(image -> image.getPostId() == postID)
                .collect(Collectors.toList());
    }

    private Path saveFile(MultipartFile file, Post post) {
        try {
            String uniqueFileName = String.format("%d.%s", Instant.now().toEpochMilli(),
                    StringUtils.getFilenameExtension(file.getOriginalFilename()));

            Path uploadPath = Paths.get(imageDirectory, String.valueOf(post.getAuthorId()),
                    post.getId().toString());
            Path filePath = uploadPath.resolve(uniqueFileName);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            file.transferTo(filePath);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

// сохранение отдельного изображения, связанного с указанным постом
        private Image saveImage(long postId, MultipartFile file) {
            Post post = postService.findPostById(postId)
                    .orElseThrow(() -> new ConditionsNotMetException("Указанный пост не найден"));

            // сохраняем изображение на диск и возвращаем путь к файлу
            Path filePath = saveFile(file, post);

            // создаём объект для хранения данных изображения
            long imageId = getNextId(images);

            // создание объекта изображения и заполнение его данными
            Image image = new Image();
            image.setId(imageId);
            image.setFilePath(filePath.toString());
            image.setPostId(postId);
            // запоминаем название файла, которое было при его передаче
            image.setOriginalFileName(file.getOriginalFilename());

            images.put(imageId, image);
            return image;
        }

        // сохранение списка изображений, связанных с указанным постом
        public List<Image> saveImages(long postID, List<MultipartFile> files) {
            return files.stream().map(file -> saveImage(postID, file)).collect(Collectors.toList());
        }


    }

