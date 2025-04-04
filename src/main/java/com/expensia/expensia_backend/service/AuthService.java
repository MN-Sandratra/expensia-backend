package com.expensia.expensia_backend.service;

import com.expensia.expensia_backend.dto.AuthResponse;
import com.expensia.expensia_backend.dto.LoginRequest;
import com.expensia.expensia_backend.dto.RegisterRequest;
import com.expensia.expensia_backend.model.User;
import com.expensia.expensia_backend.repository.UserRepository;
import com.expensia.expensia_backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserDetailServiceImpl userDetailService;

    public User register(RegisterRequest request) {
        boolean isExistUser = userRepository.existsByUsername(request.getUsername());

        if (isExistUser) {
            throw new RuntimeException("username already exists");
        }

        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).
                build();

        userRepository.save(newUser);

        return newUser;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        return new AuthResponse(token);
    }
}
