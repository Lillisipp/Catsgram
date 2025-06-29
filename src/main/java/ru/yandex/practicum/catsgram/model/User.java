package ru.yandex.practicum.catsgram.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @NotBlank(message = "Id должен быть указан")
    private Long id;

    @NotBlank
    private String username;

    @Email(regexp = ".*@.*", message = "Электронная почта должна содержать символ '@'.")
    @NotBlank(message = "Имейл должен быть указан")
    private String email;

    @NotBlank
    private String password;

    private Instant registrationDate;

}
