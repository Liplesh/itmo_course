package ru.lipnin.itmohomework.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.lipnin.itmohomework.dto.TechServRequestDTO;
import ru.lipnin.itmohomework.dto.TechServResponseDTO;

import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/services")
public class TechServController {

    /**
     * Создать v1
     * http://localhost:8081/api/v1
     * {
     *     "id": 10,
     *     "name":"Имя",
     *     "description": "Описание",
     *     "email": "test@gmail.ru"
     * }
     */
//    @PostMapping
//    public Long createTechService(@Valid @RequestBody TechServRequestDTO techServRequestDTO) {
//        return 1L;
//    }

    /**
     * Создать v2
     * http://localhost:8081/api/v1/services?id=1&name=Обслуживание&description=Описание&email=123@mail.ru
     */
    @PostMapping
    public Long createTechService(@NotNull @Positive @RequestParam Long id,
                                  @NotNull @Pattern(regexp = "^\\s*[А-Яа-яЁё]+\\s*$") @RequestParam String name,
                                  @NotEmpty @NotBlank @RequestParam(required = false) String description,
                                  @Email @RequestParam(required = false) String email) {
        return 2L;
    }

    /**
     * http://localhost:8081/api/v1/services/1
     * {
     *     "id": 10,
     *     "name":"Имя",
     *     "description": "Описание",
     *     "email": "test@gmail.ru"
     * }
     * @param id
     * @param techServRequestDTO
     * @return
     */
    @PutMapping("/{id}")
    public TechServRequestDTO updateTechService(@NotNull @Positive @PathVariable Long id,
                                                @NotNull @Valid @RequestBody TechServRequestDTO techServRequestDTO) {

        //получаем сущность из базы по id
        //обновляем ее поля из пришедшего techServRequestDTO

        return null;
    }


    /**
     * Получить все
     * http://localhost:8081/api/v1/services/all
     * @return
     */
    @GetMapping("/all")
    public List<TechServResponseDTO> getTechServices() {
        return new ArrayList<>();
    }

    /**
     * Получить по id v1
     * http://localhost:8081/api/v1/services?id=1
     * @return
     */
    @GetMapping
    public TechServResponseDTO getTechServiceById(@NotNull @Positive @RequestParam Long id) {
        return null;
    }

    /**
     * Получить по id v2
     * http://localhost:8081/api/v1/services/1
     * @return
     */
//    @GetMapping("/{id}") // /api/v1/services/1
//    public TechServResponseDTO getTechServiceById(@NotNull @Positive @PathVariable Long id) {
//        return null;
//    }


}
