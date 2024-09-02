package com.example.configs.security;

import com.example.services.CompetitionService;
import com.example.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class CompetitionSecurityFilter extends OncePerRequestFilter {
    private final static String COMPETITION_URL_START = "/competition";
    private final static String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    private final CompetitionService competitionService;
    private final JwtService jwtService;

    public CompetitionSecurityFilter(
            CompetitionService competitionService,
            JwtService jwtService
    ) {
        this.competitionService = competitionService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwt = header.substring(7);

        var uri = request.getRequestURI();
        var method = request.getMethod();

        if (!uri.startsWith(COMPETITION_URL_START)) {
            filterChain.doFilter(request, response);
        } else if (uri.equals(COMPETITION_URL_START + "/create")) {
            filterChain.doFilter(request, response);
        } else if (uri.equals(COMPETITION_URL_START + "/get_all")) {
            filterChain.doFilter(request, response);
        } else if (uri.matches(
                COMPETITION_URL_START + "/" + UUID_REGEX
        )) {
            if (method.equals("GET")) {
                filterChain.doFilter(request, response);
            } else if (method.equals("PUT") || method.equals("DELETE")) {
                try {
                    UUID competitionUuid = UUID.fromString(
                            uri.substring((COMPETITION_URL_START + "/").length())
                    );
                    UUID userUuid = jwtService.extractUUID(jwt);
                    if (competitionService.doesUserOwnCompetition(userUuid, competitionUuid)) {
                        filterChain.doFilter(request, response);
                    } else {
                        response.sendError(403, "Forbidden");
                    }
                } catch (IllegalArgumentException iae) {
                    response.sendError(404, "Wrong competition UUID");
                }
            }
        }

    }
}
