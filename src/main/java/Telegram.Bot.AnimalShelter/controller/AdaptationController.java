package Telegram.Bot.AnimalShelter.controller;

import Telegram.Bot.AnimalShelter.entity.Adaptation;
import Telegram.Bot.AnimalShelter.entity.Report;
import Telegram.Bot.AnimalShelter.service.AdaptationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("trial-periods")
@Tag(name = "Испытательный срок", description = "CRUD-методы для работы с испытательными сроками")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Всё хорошо, запрос выполнился."),
        @ApiResponse(responseCode = "400", description = "Есть ошибка в параметрах запроса."),
        @ApiResponse(responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении."),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка на сервере.")
})
public class AdaptationController {

    private final AdaptationService adaptationService;

    public AdaptationController(AdaptationService adaptationService) {
        this.adaptationService = adaptationService;
    }

    @PostMapping
    @Operation(
            summary = "Создать испытательный срок"
    )
    public Adaptation create(@RequestParam @Parameter(description = "Дата начала испытательного срока") LocalDate startDate,
                             @RequestParam @Parameter(description = "Состояние") Adaptation.Result result,
                             @RequestParam @Parameter(description = "Id хозяина животного") Long ownerId,
                             @RequestParam @Parameter(description = "Тип взятого животного") Adaptation.AnimalType animalType,
                             @RequestParam @Parameter(description = "Id животного") Long animalId) {
        return adaptationService.create(new Adaptation(ownerId, animalId, animalType, startDate, startDate.plusDays(30),
                startDate.minusDays(1), new ArrayList<>(), result));
    }

    @GetMapping()
    @Operation(
            summary = "Получение всех испытательных сроков"
    )
    public List<Adaptation> getAll() {
        return adaptationService.getAll();
    }

    @GetMapping("owner")
    @Operation(summary = "Получение всех испытательных сроков по id хозяина")
    public List<Adaptation> getAllByOwnerId(@RequestParam @Parameter(description = "Id хозяина животного") Long ownerId) {
        return adaptationService.getAllByOwnerId(ownerId);
    }

    @GetMapping("id")
    @Operation(
            summary = "Получение испытательного срока по id"
    )
    @Parameter(
            name = "id",
            description = "Id ипытательного срока",
            example = "1"
    )
    public Adaptation getById(@RequestParam Long id) {
        return adaptationService.getById(id);
    }

    @PutMapping
    @Operation(
            summary = "Изменить испытательный срок"
    )
    public Adaptation update(@RequestParam @Parameter(description = "Id испытательного срока") Long id,
                              @RequestParam(required = false) @Parameter(description = "Дата начала испытательного срока") LocalDate startDate,
                              @RequestParam(required = false) @Parameter(description = "Дата окончания испытательного срока") LocalDate endDate,
                              @RequestParam(required = false) @Parameter(description = "Дата последнего отчёта") LocalDate lastReportDate,
                              @RequestParam(required = false) @Parameter(description = "Состояние") Adaptation.Result result,
                              @RequestParam(required = false) @Parameter(description = "Id хозяина животного") Long ownerId,
                              @RequestParam(required = false) @Parameter(description = "Тип взятого животного") Adaptation.AnimalType animalType,
                              @RequestParam(required = false) @Parameter(description = "Id животного") Long animalId) {
        return adaptationService.update(new Adaptation(new ArrayList<>(), id, ownerId, animalId, animalType, startDate, endDate,
                lastReportDate, result));
    }

    @DeleteMapping("id")
    @Operation(summary = "Удаление испытательного срока по id")
    public String deleteById(@RequestParam @Parameter(description = "Id испытательного срока") Long id) {
        adaptationService.deleteById(id);
        return "Испытательный срок успешно удалён";
    }
}
