package Telegram.Bot.AnimalShelter.controller;

import Telegram.Bot.AnimalShelter.entity.Volunteer;
import Telegram.Bot.AnimalShelter.service.VolunteerService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("volunteers")
@Tag(name = "Volunteer", description = "CRUD-methods for Volunteer Class")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully done."),
        @ApiResponse(responseCode = "400", description = "There is a mistake in params."),
        @ApiResponse(responseCode = "404", description = "URL is incorrect or there is no such action."),
        @ApiResponse(responseCode = "500", description = "Mistake occur on server during the process.")
})
public class VolunteerController {
    private final VolunteerService volunteerService;

    private final TelegramBot telegramBot;

    public VolunteerController(VolunteerService volunteerService, TelegramBot telegramBot) {
        this.volunteerService = volunteerService;
        this.telegramBot = telegramBot;
    }

    @PostMapping
    @Operation(summary = "Create volunteer")
    public Volunteer create(@RequestParam @Parameter(description = "Volunteer telegram ID") Long telegramId,
                            @RequestParam @Parameter(description = "Volunteer name") String firstName,
                            @RequestParam @Parameter(description = "Volunteer last name") String lastName) {
        return volunteerService.create(new Volunteer(telegramId, firstName, lastName));
    }

    @GetMapping()
    @Operation(summary = "Return list of all volunteer")
    public List<Volunteer> getAll() {
        return volunteerService.getAll();
    }

    @GetMapping("id")
    @Operation(summary = "Return volunteer by ID")
    public Volunteer getById(@RequestParam @Parameter(description = "volunteer ID") Long volunteerId) {
        return volunteerService.getById(volunteerId);
    }

    @PutMapping
    @Operation(summary = "Update volunteer")
    public Volunteer update(@RequestParam @Parameter(description = "Volunteer telegram ID") Long telegramId,
                            @RequestParam(required = false) @Parameter(description = "Volunteer name") String firstName,
                            @RequestParam(required = false) @Parameter(description = "Volunteer last name") String lastName) {
        return volunteerService.update(new Volunteer(telegramId, firstName, lastName));
    }

    @DeleteMapping("id")
    @Operation(summary = "Remove volunteer by ID")
    public String deleteById(@RequestParam @Parameter(description = "volunteer ID") Long volunteerId) {
        volunteerService.deleteById(volunteerId);
        return "Volunteer removed successfully";
    }

    @Tag(name = "User message")
    @PostMapping("warning-message")
    @Operation(summary = "Send a warning")
    public String sendWarning(@RequestParam @Parameter(description = "Owner ID") Long ownerId) {
        telegramBot.execute(new SendMessage(ownerId,
                "Hi, we noticed, that you are not doing a good job with reports. Please, send correct reports or we will be obliged to check on you offline."));
        return "Message sent successfully";
    }
}

