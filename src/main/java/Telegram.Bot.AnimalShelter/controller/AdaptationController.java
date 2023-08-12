package Telegram.Bot.AnimalShelter.controller;

import Telegram.Bot.AnimalShelter.entity.Adaptation;
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
@RequestMapping("adaptation-periods")
@Tag(name = "Adaptation", description = "CRUD-methods for Adaptation Class")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully done."),
        @ApiResponse(responseCode = "400", description = "There is a mistake in params."),
        @ApiResponse(responseCode = "404", description = "URL is incorrect or there is no such action."),
        @ApiResponse(responseCode = "500", description = "Mistake occur on server during the process.")
})
public class AdaptationController {

    private final AdaptationService adaptationService;

    public AdaptationController(AdaptationService adaptationService) {
        this.adaptationService = adaptationService;
    }

    @PostMapping
    @Operation(summary = "Create adaptation")
    public Adaptation create(@RequestParam @Parameter(description = "Start date of adaptation") LocalDate startDate,
                             @RequestParam @Parameter(description = "Adaptation state") Adaptation.Result result,
                             @RequestParam @Parameter(description = "Owner ID") Long ownerId,
                             @RequestParam @Parameter(description = "Animal type") Adaptation.AnimalType animalType,
                             @RequestParam @Parameter(description = "Animal ID") Long animalId) {
        return adaptationService.create(new Adaptation(ownerId, animalId, animalType, startDate, startDate.plusDays(30),
                startDate.minusDays(1), new ArrayList<>(), result));
    }

    @GetMapping()
    @Operation(summary = "Return list with all adaptations")
    public List<Adaptation> getAll() {
        return adaptationService.getAll();
    }

    @GetMapping("owner")
    @Operation(summary = "Return list with all adaptations by owner ID")
    public List<Adaptation> getAllByOwnerId(@RequestParam @Parameter(description = "Owner ID") Long ownerId) {
        return adaptationService.getAllByOwnerId(ownerId);
    }

    @GetMapping("id")
    @Operation(summary = "Return adaptation by ID")
    public Adaptation getById(@RequestParam Long id) {
        return adaptationService.getById(id);
    }

    @PutMapping
    @Operation(summary = "Update adaptation info")
    public Adaptation update(@RequestParam @Parameter(description = "Adaptation Id") Long id,
                              @RequestParam(required = false) @Parameter(description = "Start date of adaptation") LocalDate startDate,
                              @RequestParam(required = false) @Parameter(description = "End date of adaptation") LocalDate endDate,
                              @RequestParam(required = false) @Parameter(description = "Date of last report") LocalDate dateOfLastReport,
                              @RequestParam(required = false) @Parameter(description = "Adaptation state") Adaptation.Result result,
                              @RequestParam(required = false) @Parameter(description = "Owner ID") Long ownerId,
                              @RequestParam(required = false) @Parameter(description = "Animal type") Adaptation.AnimalType animalType,
                              @RequestParam(required = false) @Parameter(description = "Animal ID") Long animalId) {
        return adaptationService.update(new Adaptation(new ArrayList<>(), id, ownerId, animalId, animalType, startDate, endDate,
                dateOfLastReport, result));
    }

    @DeleteMapping("id")
    @Operation(summary = "Remove adaptation by ID")
    public String deleteById(@RequestParam @Parameter(description = "Adaptation ID") Long id) {
        adaptationService.deleteById(id);
        return "Adaptation removed successfully";
    }
}
