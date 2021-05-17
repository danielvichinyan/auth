package com.knowit.auth.controllers;

import com.knowit.auth.constants.AuthenticationConstants;
import com.knowit.auth.domain.models.LoginRequestModel;
import com.knowit.auth.domain.models.RegistrationRequestModel;
import com.knowit.auth.domain.models.RegistrationResponseModel;
import com.knowit.auth.exceptions.PasswordsDoNotMatchException;
import com.knowit.auth.exceptions.WrongCredentialsException;
import com.knowit.auth.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponseModel userRegistration(
            @RequestBody RegistrationRequestModel request
    ) throws PasswordsDoNotMatchException {
        return this.userService.register(request);
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public void login(
            @RequestBody LoginRequestModel request,
            HttpServletResponse httpServletResponse
    ) throws WrongCredentialsException {
        httpServletResponse.addHeader(
                AuthenticationConstants.AUTHORIZATION,
                AuthenticationConstants.BEARER.concat(this.userService.loginUser(request))
        );
    }
}
