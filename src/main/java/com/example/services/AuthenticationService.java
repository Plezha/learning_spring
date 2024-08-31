package com.example.services;


import com.example.entities.User;
import com.example.repositories.UserRepository;
import org.openapitools.model.UserLoginRequest;
import org.openapitools.model.UserSignupRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(UserSignupRequest request) {
        User user = new User(
                UUID.randomUUID(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        return userRepository.save(user);
    }

    public User authenticate(UserLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        return userRepository.findFirstByEmail(request.getEmail());
    }
}