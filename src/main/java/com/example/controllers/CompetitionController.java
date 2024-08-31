package com.example.controllers;

import org.openapitools.api.CompetitionApi;
import org.openapitools.model.CompetitionCreateRequest;
import org.openapitools.model.CompetitionEditRequest;
import org.openapitools.model.CompetitionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CompetitionController implements CompetitionApi {
    @Override
    public ResponseEntity<CompetitionResponse> competitionCompetitionUuidDelete(String competitionUuid) {
        return null;
    }

    @Override
    public ResponseEntity<CompetitionResponse> competitionCompetitionUuidGet(String competitionUuid) {
        return null;
    }

    @Override
    public ResponseEntity<CompetitionResponse> competitionCompetitionUuidPut(String competitionUuid, CompetitionEditRequest competitionEditRequest) {
        return null;
    }

    @Override
    public ResponseEntity<CompetitionResponse> competitionCreatePost(CompetitionCreateRequest competitionCreateRequest) {
        return null;
    }

    @Override
    public ResponseEntity<List<CompetitionResponse>> competitionGetAllGet() {
        return null;
    }
}
