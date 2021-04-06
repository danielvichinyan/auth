package com.knowit.auth.controllers;

import com.knowit.auth.constants.AuthConstants;
import com.knowit.auth.domain.models.LoginRequest;
import com.knowit.auth.domain.models.RegisterRequest;
import com.knowit.auth.domain.models.RegisterResponse;
import com.knowit.auth.exceptions.PasswordsDoNotMatchException;
import com.knowit.auth.exceptions.WrongCredentialsException;
import com.knowit.auth.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register (
            @RequestBody RegisterRequest request
    ) throws PasswordsDoNotMatchException {
        return this.userService.register(request);
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public void login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) throws WrongCredentialsException {
        response.addHeader(
                AuthConstants.AUTHORIZATION,
                AuthConstants.BEARER.concat(this.userService.login(request))
        );
    }
}
