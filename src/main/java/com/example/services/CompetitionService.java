package com.example.services;

import com.example.entities.Competition;
import com.example.repositories.CompetitionRepository;
import org.openapitools.model.CompetitionEditRequest;
import org.openapitools.model.CompetitionResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompetitionService {
    private final CompetitionRepository competitionRepository;

    public CompetitionService(
            CompetitionRepository competitionRepository
    ) {
        this.competitionRepository = competitionRepository;
    }

    public CompetitionResponse deleteByUUID(UUID uuid) {
        return asCompetitionResponse(competitionRepository.deleteByUuid(uuid));
    }

    public CompetitionResponse getByUUID(UUID uuid) {
        return asCompetitionResponse(competitionRepository.getByUuid(uuid));
    }

    public CompetitionResponse create(Competition competition) {
        return asCompetitionResponse(
                competitionRepository.save(competition)
        );
    }

    public CompetitionResponse editCompetition(UUID competitionId, CompetitionEditRequest competitionEditRequest) {
        return asCompetitionResponse(
                competitionRepository.changeCompetitionName(competitionId, competitionEditRequest.getName())
        );
    }

    public List<CompetitionResponse> getAllCompetitions() {
        return competitionRepository.getAllCompetitions().stream()
                .map(CompetitionService::asCompetitionResponse)
                .toList();
    }

    public Boolean doesUserOwnCompetition(UUID userId, UUID competitionId) {
        return competitionRepository.doesUserOwnCompetition(userId, competitionId);
    }

    private static CompetitionResponse asCompetitionResponse(Competition competition) {
        return new CompetitionResponse()
                .uuid(competition.getUuid().toString())
                .name(competition.getName())
                .owners(
                        competition.getOwners().stream()
                                .map(UUID::toString)
                                .toList()
                );
    }
}
