package com.aftas_backend.web.rest.controllers;

import com.aftas_backend.handlers.exceptionHandler.OperationException;
import com.aftas_backend.handlers.response.ResponseMessage;
import com.aftas_backend.models.entities.Competition;
import com.aftas_backend.services.CompetitionService;
import com.aftas_backend.web.rest.vms.competition.CompetitionRequestVM;
import com.aftas_backend.web.rest.vms.competition.CompetitionResponseVM;
import jakarta.validation.Valid;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/competitions")
public class CompetitionController {
    private CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }


    @PreAuthorize(value = "hasRole('MANAGER') or hasRole('JURY')")
    @GetMapping
    public ResponseEntity getAllCompetitionsj(@ParameterObject Pageable pageable, @RequestParam(required = false, name = "search") String search,
                                              @RequestParam(required = false) LocalDate date) {

        List<Competition> competitions = competitionService.getAllCompetitions(pageable, search, date);
        List<CompetitionResponseVM> competitionResponseVMS = new ArrayList<>();
        for (Competition competition : competitions) {
            competitionResponseVMS.add(CompetitionResponseVM.fromCompetition(competition));
        }
        return ResponseMessage.ok(competitionResponseVMS, "Competitions retrieved successfully");
    }

    @PreAuthorize(value = "hasRole('MANAGER') or hasRole('JURY')")
    @PostMapping()
    public ResponseEntity createCompetition(@Valid @RequestBody CompetitionRequestVM competitionVM) {
        Competition createdCompetition = competitionService.createCompetition(competitionVM.toCompetition());
        CompetitionResponseVM competitionResponseVM = CompetitionResponseVM.fromCompetition(createdCompetition);
        return ResponseMessage.created(competitionResponseVM, "Competition created successfully");
    }

    @PreAuthorize(value = "hasRole('MANAGER') or hasRole('JURY')")
    @GetMapping("/{id}")
    public ResponseEntity getCompetitionById(@PathVariable String id) {
        Competition competition = competitionService.getCompetitionById(id);
        CompetitionResponseVM competitionResponseVM = CompetitionResponseVM.fromCompetition(competition);
        return ResponseMessage.ok(competitionResponseVM, "Competition retrieved successfully");
    }

    @PreAuthorize(value = "hasRole('MANAGER') or hasRole('JURY')")
    @PutMapping("/{id}")
    public ResponseEntity updateCompetition(@Valid @RequestBody CompetitionRequestVM competitionVM, @PathVariable String id) {
        Competition updatedCompetition = competitionService.updateCompetition(competitionVM.toCompetition(), id);
        CompetitionResponseVM competitionResponseVM = CompetitionResponseVM.fromCompetition(updatedCompetition);
        return ResponseMessage.ok(competitionResponseVM, "Competition updated successfully");
    }

    @PreAuthorize(value = "hasRole('MANAGER') or hasRole('JURY')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCompetition(@PathVariable String id) {
        competitionService.deleteCompetition(id);
        return ResponseMessage.ok(null, "Competition deleted successfully");
    }

    @PreAuthorize(value = "hasRole('MANAGER') or hasRole('JURY')")
    @GetMapping("/count")
    public ResponseEntity countCompetitions() {
        Long count = competitionService.countCompetitions();
        return ResponseMessage.ok(count, "Competitions counted successfully");
    }



}
