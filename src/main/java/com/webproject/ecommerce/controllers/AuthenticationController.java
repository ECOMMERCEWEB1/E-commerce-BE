package com.webproject.ecommerce.controllers;

import com.webproject.ecommerce.config.JwtService;
import com.webproject.ecommerce.dto.AuthenticationResponseDTO;
import com.webproject.ecommerce.dto.LoginDTO;
import com.webproject.ecommerce.dto.MessageDTO;
import com.webproject.ecommerce.dto.SignUpDTO;
import com.webproject.ecommerce.entities.User;
import com.webproject.ecommerce.mappers.UserMapper;
import com.webproject.ecommerce.repositories.UsersRepository;
import com.webproject.ecommerce.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor

public class AuthenticationController {
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    private final UsersRepository usersRepository;
    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    @CrossOrigin(value = "http://localhost:4200",allowCredentials = "true")
    @PostMapping("/signup")
    public ResponseEntity<MessageDTO> register(
            @RequestBody SignUpDTO userInformation
    ) {
        if (usersRepository.existsByEmail(userInformation.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageDTO("Email Already in Use!"));
        return ResponseEntity.ok(authenticationService.register(userInformation));
    }

    @CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(
            HttpServletResponse response,
            @RequestBody LoginDTO userCredentials
    ) {
        try{
            AuthenticationResponseDTO auth = authenticationService.login(userCredentials);
            log.debug("REST request to authenticate user with email : {}", userCredentials.getEmail());
            Cookie jwtCookie = new Cookie("jwt", auth.getToken());
            jwtCookie.setMaxAge(60 * 60 * 24);
            jwtCookie.setHttpOnly(true);
            response.addCookie(jwtCookie);
            return ResponseEntity.ok(auth);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(new AuthenticationResponseDTO(null,e.getMessage(),null));
        }

    }

    @CrossOrigin(value="http://localhost:4200",allowCredentials = "true")
    @GetMapping("/auth")
    public ResponseEntity<?> getAuthenticatedUserInfo(HttpServletRequest request) {
        String jwtToken = jwtService.getJwtFromCookies(request);
        User user = authenticationService.findAuthenticatedUser(jwtToken);
        if (user == null) {
            return ResponseEntity.status(401).body(new MessageDTO("User is not authenticated"));
        }
        return ResponseEntity.status(200).body(userMapper.toDto(user,"User loaded successfully"));

    }
}
