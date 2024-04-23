package com.webproject.ecommerce.controllers;

import com.webproject.ecommerce.dto.AuthenticationResponseDTO;
import com.webproject.ecommerce.dto.LoginDTO;
import com.webproject.ecommerce.dto.MessageDTO;
import com.webproject.ecommerce.dto.SignUpDTO;
import com.webproject.ecommerce.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<MessageDTO> register(
            @RequestBody SignUpDTO userInformation
            )
    {
        return ResponseEntity.ok(authenticationService.register(userInformation));
    }
    @CrossOrigin(allowCredentials = "true")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> register(
            HttpServletResponse response,
            @RequestBody LoginDTO userCredentials
    )
    {
        AuthenticationResponseDTO auth = authenticationService.login(userCredentials);
        Cookie jwtCookie = new Cookie("jwt",auth.getToken());
        jwtCookie.setMaxAge(60*60*24);
        Cookie roleCookie = new Cookie("isAdmin", auth.getIsAdmin().toString());
        jwtCookie.setMaxAge(60*60*24);
        jwtCookie.setHttpOnly(true);
        roleCookie.setHttpOnly(true);
        response.addCookie(jwtCookie);
        response.addCookie(roleCookie);

        return ResponseEntity.ok(auth);
    }
}
