package com.example.controllers;

import com.example.entities.User;
import com.example.services.AuthenticationService;
import com.example.services.JwtService;
import org.openapitools.api.UserApi;
import org.openapitools.model.UserLoginRequest;
import org.openapitools.model.UserLoginResponse;
import org.openapitools.model.UserResponse;
import org.openapitools.model.UserSignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public UserController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @Override
    public ResponseEntity<UserLoginResponse> userAuthLoginPut(UserLoginRequest userLoginRequest) {
        User authenticatedUser = authenticationService.authenticate(userLoginRequest);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        UserLoginResponse userLoginResponse = new UserLoginResponse()
                .jwt(jwtToken)
                .expiresInMilliseconds(jwtService.getExpirationTime());

        return ResponseEntity.ok(userLoginResponse);
    }

    @Override
    public ResponseEntity<UserResponse> userAuthSignupPut(UserSignupRequest userSignupRequest) {
        User registeredUser = authenticationService.signup(userSignupRequest);
        UserResponse userResponse = new UserResponse()
                .uuid(registeredUser.getUuid())
                .email(registeredUser.getEmail())
                .firstName(registeredUser.getFirstName())
                .lastName(registeredUser.getLastName());

        return ResponseEntity.ok(userResponse);
    }

    @Override
    public ResponseEntity<UserResponse> userMeGet() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();
        System.out.println(currentUser);

        return ResponseEntity.ok(new UserResponse());
    }
}