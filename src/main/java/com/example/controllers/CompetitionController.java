package com.example.controllers;

import com.example.entities.Competition;
import com.example.entities.User;
import com.example.repositories.CompetitionRepository;
import com.example.services.CompetitionService;
import org.openapitools.api.CompetitionApi;
import org.openapitools.model.CompetitionCreateRequest;
import org.openapitools.model.CompetitionEditRequest;
import org.openapitools.model.CompetitionResponse;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class CompetitionController implements CompetitionApi {
    CompetitionService competitionService;

    public CompetitionController(
            CompetitionService competitionService
    ) {
        this.competitionService = competitionService;
    }

    @Override
    public ResponseEntity<CompetitionResponse> competitionCompetitionUuidDelete(String competitionUuid) {
        return ResponseEntity.ok(
                competitionService.deleteByUUID(UUID.fromString(competitionUuid))
        );
    }

    @Override
    public ResponseEntity<CompetitionResponse> competitionCompetitionUuidGet(String competitionUuid) {
        return ResponseEntity.ok(
                competitionService.getByUUID(UUID.fromString(competitionUuid))
        );
    }

    @Override
    public ResponseEntity<CompetitionResponse> competitionCompetitionUuidPut(String competitionUuid, CompetitionEditRequest competitionEditRequest) {
        return ResponseEntity.ok(
                competitionService.editCompetition(UUID.fromString(competitionUuid), competitionEditRequest)
        );
    }

    @Override
    public ResponseEntity<CompetitionResponse> competitionCreatePost(CompetitionCreateRequest competitionCreateRequest) {
        return ResponseEntity.ok(
                competitionService.create(
                        new Competition(
                                UUID.randomUUID(),
                                competitionCreateRequest.getName(),
                                List.of(getCurrentUserUUID())
                        )
                )
        );
    }

    @Override
    public ResponseEntity<List<CompetitionResponse>> competitionGetAllGet() {
        return ResponseEntity.ok(
                competitionService.getAllCompetitions()
        );
    }

    private static UUID getCurrentUserUUID() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUuid();
    }
}
