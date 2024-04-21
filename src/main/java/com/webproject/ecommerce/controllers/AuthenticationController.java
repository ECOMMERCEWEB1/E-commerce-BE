package com.webproject.ecommerce.controllers;

import com.webproject.ecommerce.dto.AuthenticationResponseDTO;
import com.webproject.ecommerce.dto.LoginDTO;
import com.webproject.ecommerce.dto.SignUpDTO;
import com.webproject.ecommerce.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponseDTO> register(
            @RequestBody SignUpDTO userInformation
            )
    {
        return ResponseEntity.ok(authenticationService.register(userInformation));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> register(
            @RequestBody LoginDTO userCredentials
    )
    {
        return ResponseEntity.ok(authenticationService.login(userCredentials));
    }
}
