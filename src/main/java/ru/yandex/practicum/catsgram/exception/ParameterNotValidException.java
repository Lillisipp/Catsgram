package ru.yandex.practicum.catsgram.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ParameterNotValidException extends IllegalArgumentException {
    private String parameter;
    private String reason;

    public ParameterNotValidException(String parameter, String reason) {
        super("Ошибка параметра: " + parameter + " — " + reason);
        this.parameter = parameter;
        this.reason = reason;
    }
}
