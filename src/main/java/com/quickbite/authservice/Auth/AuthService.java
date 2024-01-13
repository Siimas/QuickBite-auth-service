package com.quickbite.authservice.Auth;

import com.quickbite.authservice.Dto.*;
import com.quickbite.authservice.Entities.User.User;
import com.quickbite.authservice.Entities.User.UserRepository;
import com.quickbite.authservice.Entities.User.UserRole;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;
    private final JwtUtils jwtUtils;

    public UserDTO signup(SignupRequest signupRequest) {
        User user = User.builder()
                .firstname(signupRequest.firstname())
                .lastname(signupRequest.lastname())
                .email(signupRequest.email())
                .password(passwordEncoder.encode(signupRequest.password()))
                .age(signupRequest.age())
                .role(UserRole.USER)
                .tag("test") // TODO: generate tag
                .build();

        userRepository.save(user);

        return userDTOMapper.apply(user);
    }

    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        var user = userRepository.findByEmail(signinRequest.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        var token = jwtUtils.generateToken(user.getEmail());
        var refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

        return new JwtAuthenticationResponse(
                token,
                refreshToken
        );
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtUtils.extractEmail(refreshTokenRequest.token());
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        if (!jwtUtils.validateToken(refreshTokenRequest.token(), userEmail)) {
            return null;
        }

        var token = jwtUtils.generateToken(user.getEmail());

        return new JwtAuthenticationResponse(
                token,
                refreshTokenRequest.token()
        );
    }
}