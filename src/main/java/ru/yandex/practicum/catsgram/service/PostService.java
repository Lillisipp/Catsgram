package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.enums.SortOrder;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.model.enums.SortOrder;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.catsgram.utils.Utils.getNextId;

@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;

    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAll(int from,int size, SortOrder sortOrder ) {
        Comparator<Post> comparator=Comparator.comparing(Post::getPostDate);
        if (sortOrder == SortOrder.DESCENDING) {
            comparator = comparator.reversed();
        }

        return posts.values().stream()
                .sorted(comparator)
                .skip(from)         // Пропускаем указанные элементы
                .limit(size)        // Ограничиваем количество
                .collect(Collectors.toList());
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        Long authorId = post.getAuthorId();
        if (authorId == null || userService.findUserById(authorId).isEmpty()) {
            throw new ConditionsNotMetException("Автор с id = " + authorId + " не найден");
        }
        post.setId(getNextId(posts));
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    public Optional<Post> findPostById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }
}