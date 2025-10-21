package ru.lipnin.itmohomework.dto;

import jakarta.validation.constraints.*;

public class TechServRequestDTO {

    @NotNull
    @Positive
    private Long id;

    @NotNull
    @Pattern(regexp = "^\\s*[А-Яа-яЁё]+\\s*$")
    private String name;

    @NotNull
    @NotBlank
    @NotEmpty
    private String description;

    @NotNull
    @NotBlank
    @NotEmpty
    @Email
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


