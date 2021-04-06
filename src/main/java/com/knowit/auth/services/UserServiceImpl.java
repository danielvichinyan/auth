package com.knowit.auth.services;

import com.knowit.auth.domain.entities.User;
import com.knowit.auth.domain.models.LoginRequest;
import com.knowit.auth.domain.models.RegisterRequest;
import com.knowit.auth.domain.models.RegisterResponse;
import com.knowit.auth.exceptions.PasswordsDoNotMatchException;
import com.knowit.auth.exceptions.WrongCredentialsException;
import com.knowit.auth.jwt.JwtImpl;
import com.knowit.auth.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtImpl jwtUtils;

    public UserServiceImpl(
            UserRepository userRepository,
            ModelMapper modelMapper,
            PasswordEncoder passwordEncoder,
            JwtImpl jwtUtils) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) throws PasswordsDoNotMatchException {
        return null;
    }

    @Override
    public String login(LoginRequest request) throws WrongCredentialsException {
        User user = this.userRepository.findFirstByUsername(request.getUsername());

        if (!this.passwordEncoder.matches(request.getPassword(), user.getPassword()) ||
                !request.getUsername().equals(user.getUsername())) {
            throw new WrongCredentialsException();
        }

        return this.jwtUtils.generateJwtToken(user);
    }
}
