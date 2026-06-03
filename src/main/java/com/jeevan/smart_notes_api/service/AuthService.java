package com.jeevan.smart_notes_api.service;

import com.jeevan.smart_notes_api.dto.AuthResponse;
import com.jeevan.smart_notes_api.dto.LoginRequest;
import com.jeevan.smart_notes_api.dto.RegisterRequest;
import com.jeevan.smart_notes_api.entity.RefreshToken;
import com.jeevan.smart_notes_api.entity.User;
import com.jeevan.smart_notes_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class AuthService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder(12);

    public User register(RegisterRequest request) {

        if(repository.findByUsername(
                request.getUsername()).isPresent()) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        User user = new User();
        user.setName(request.getName());

        user.setUsername(request.getUsername());

        user.setPassword(
                encoder.encode(request.getPassword())
        );

        user.setRole("ROLE_USER");

        return repository.save(user);
    }

    public AuthResponse verify(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        if (authentication.isAuthenticated()) {

            String accessToken =
                    jwtService.generateToken(
                            request.getUsername()
                    );

            String refreshToken =
                    refreshTokenService
                            .createRefreshToken(
                                    request.getUsername()
                            )
                            .getToken();

            return new AuthResponse(
                    accessToken,
                    refreshToken
            );
        }

        throw new RuntimeException("Invalid login");
    }

    public AuthResponse refreshToken(String requestToken) {

        RefreshToken refreshToken =
                refreshTokenService
                        .verifyRefreshToken(requestToken);

        String accessToken =
                jwtService.generateToken(
                        refreshToken.getUsername()
                );

        return new AuthResponse(
                accessToken,
                refreshToken.getToken()
        );
    }

    public String logout(String refreshToken) {

        refreshTokenService.deleteByToken(refreshToken);

        return "Logged out successfully";
    }
}