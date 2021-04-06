package com.knowit.auth.services;

import com.knowit.auth.constants.KafkaConstants;
import com.knowit.auth.domain.entities.User;
import com.knowit.auth.domain.models.LoginRequest;
import com.knowit.auth.domain.models.RegisterRequest;
import com.knowit.auth.domain.models.RegisterResponse;
import com.knowit.auth.domain.models.RegisterUserModel;
import com.knowit.auth.exceptions.PasswordsDoNotMatchException;
import com.knowit.auth.exceptions.WrongCredentialsException;
import com.knowit.auth.jwt.JwtImpl;
import com.knowit.auth.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtImpl jwtUtils;

    private final StreamBridge streamBridge;

    public UserServiceImpl(
            UserRepository userRepository,
            ModelMapper modelMapper,
            PasswordEncoder passwordEncoder,
            JwtImpl jwtUtils, StreamBridge streamBridge) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.streamBridge = streamBridge;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) throws PasswordsDoNotMatchException {
        User user = new User();
        user.setUsername(request.getUsername());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        this.userRepository.saveAndFlush(user);

        RegisterUserModel userModel = this.modelMapper.map(request, RegisterUserModel.class);
        User userId = this.userRepository.findFirstByUsername(request.getUsername());
        userModel.setId(userId.getId());
        this.streamBridge.send(KafkaConstants.USER_PUBLISHER, userModel);

        return this.modelMapper.map(user, RegisterResponse.class);
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

    @Bean
    public Supplier<Mono<RegisterUserModel>> publishUser() {
        return () -> Mono.just(new RegisterUserModel());
    }
}
