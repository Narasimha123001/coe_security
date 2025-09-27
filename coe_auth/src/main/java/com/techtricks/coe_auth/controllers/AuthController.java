package com.techtricks.coe_auth.controllers;
import com.techtricks.coe_auth.dtos.UserDto;
import com.techtricks.coe_auth.dtos.UserResponseDto;
import com.techtricks.coe_auth.models.User;
import com.techtricks.coe_auth.services.UserService;
import com.techtricks.coe_auth.utils.JWTUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {



    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    public AuthController(PasswordEncoder passwordEncoder, JWTUtil jwtUtil, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User  user) {
        userService.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User created successfully");
    }

    @PostMapping("/authenticate")
    public String authenticate(@Valid @RequestBody UserDto user) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            return jwtUtil.generateToken(user.getUsername());
        }catch(AuthenticationException e){
            throw new RuntimeException("Invalid username or password");
        }
    }
}
