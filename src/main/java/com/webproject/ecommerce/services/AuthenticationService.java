package com.webproject.ecommerce.services;

import com.webproject.ecommerce.config.JwtService;
import com.webproject.ecommerce.dto.AuthenticationResponseDTO;
import com.webproject.ecommerce.dto.LoginDTO;
import com.webproject.ecommerce.dto.MessageDTO;
import com.webproject.ecommerce.dto.SignUpDTO;
import com.webproject.ecommerce.entities.User;
import com.webproject.ecommerce.repositories.UsersRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UsersRepository usersRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public MessageDTO register(SignUpDTO userInformation){
        User user = new User(
                userInformation.getFirstName(),
                userInformation.getLastName(),
                userInformation.getEmail(),
                new BCryptPasswordEncoder().encode(userInformation.getPassword()),
                userInformation.getAge(),
                true
        );
        usersRepository.save(user);
        String jwt = jwtService.generateJwt(user);
        return new MessageDTO("User signed up successfully !");
    }
    public AuthenticationResponseDTO login(LoginDTO credentials){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getEmail(),credentials.getPassword())
        );
        User user = usersRepository.findUserByEmail(credentials.getEmail()).orElseThrow(() -> new UsernameNotFoundException("This user does not exist !"));
        String jwt = jwtService.generateJwt(user);
        return new AuthenticationResponseDTO(jwt,"User logged in successfully !", user.getRole().name().equals("ADMIN"));
    }

    public User findAuthenticatedUser(String jwtToken) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails){

            if (jwtService.isTokenValid(jwtToken, userDetails)){
             String username = jwtService.extractUsername(jwtToken);
             Optional<User> user = usersRepository.findUserByEmail(username);
                return user.orElse(null);
        }
        }
        return null;
    }
}
