package ru.yandex.practicum.catsgram.utils;

import java.util.Map;

public class Utils {
//    static int getNextId(Map<Integer, ?> map) {
//        return map.keySet().stream()
//                .mapToInt(id -> id)
//                .max()
//                .orElse(0) + 1;
//    }

    public static Long getNextId(Map<Long, ?> map) {
        return map.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0L) + 1L;
    }

}
