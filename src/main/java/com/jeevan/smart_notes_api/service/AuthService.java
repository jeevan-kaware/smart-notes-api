package com.jeevan.smart_notes_api.service;

import com.jeevan.smart_notes_api.dto.response.AuthResponse;
import com.jeevan.smart_notes_api.dto.request.LoginRequest;
import com.jeevan.smart_notes_api.dto.request.RegisterRequest;
import com.jeevan.smart_notes_api.entity.RefreshToken;
import com.jeevan.smart_notes_api.entity.User;
import com.jeevan.smart_notes_api.repository.UserRepository;
import com.jeevan.smart_notes_api.security.jwt.JwtService;
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

        if(repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());

        user.setEmail(request.getEmail());

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
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        if (authentication.isAuthenticated()) {

            String accessToken =
                    jwtService.generateToken(
                            request.getEmail()
                    );

            String refreshToken =
                    refreshTokenService
                            .createRefreshToken(
                                    request.getEmail()
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
                        refreshToken.getEmail()
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