package com.webproject.ecommerce.services;

import com.webproject.ecommerce.config.JavaMailConfig;
import com.webproject.ecommerce.config.JwtService;
import com.webproject.ecommerce.dto.AuthenticationResponseDTO;
import com.webproject.ecommerce.dto.LoginDTO;
import com.webproject.ecommerce.dto.MessageDTO;
import com.webproject.ecommerce.dto.SignUpDTO;
import com.webproject.ecommerce.entities.User;
import com.webproject.ecommerce.repositories.UsersRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UsersRepository usersRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailConfig javaMailConfig;
    private final UsersService usersService;
    final HashMap<Long,String> resetTokens = new HashMap<>();
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
        System.out.println("AUTHENTICATED IN SERVER");
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
                if (user.get() != null){
                    return user.get();
                }
                return null;
        }
        }
        return null;
    }
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        // Set its maximum age to 0, effectively expiring it
        cookie.setMaxAge(0);
        // Set the cookie's path (if it's different from the default path)
        // cookie.setPath("/your/path");
        // Add the cookie to the response
        response.addCookie(cookie);
    }
    public void sendReset(String email) throws MessagingException {
        Optional<User> user = usersService.findByEmail(email);
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            resetTokens.put(user.get().getId(),token);
            JavaMailSender sender = javaMailConfig.getJavaMailSender();
            MimeMessage message = sender.createMimeMessage();
            message.setSubject("TechTemple - Reset Password");
            String bodyHtml = "<!DOCTYPE html>" +
                    "<html lang=\"en\">" +
                    "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "<title>Password Reset</title>" +
                    "<style>" +
                    "body {" +
                    "  font-family: Arial, sans-serif;" +
                    "  background: #DFF5FF !important;" +
                    "  margin: 0;" +
                    "  padding: 0;" +
                    "  display: flex;" +
                    "  justify-content: center;" +
                    "  align-items: center;" +
                    "  height: 40vh;" +
                    "}" +
                    ".container {" +
                    "  background-color: white;" +
                    "  border-radius: 25px;" +
                    "  box-shadow: 3px 6px 72px 5px rgba(74,74,74,0.76);" +
                    "  -webkit-box-shadow: 3px 6px 72px 5px rgba(74,74,74,0.76);" +
                    "  -moz-box-shadow: 3px 6px 72px 5px rgba(74,74,74,0.76);" +
                    "  padding: 40px;" +
                    "  height: min-content;" +
                    "  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);" +
                    "  text-align: center;" +
                    "}" +
                    ".logo img {" +
                    "  width: 100px;" +
                    "  margin-bottom: 20px;" +
                    "}" +
                    "h2 {" +
                    "  color: #5356FF;" +
                    "  margin-bottom: 20px;" +
                    "}" +
                    ".text-principal{" +
                    "color: #5356FF;" +
                    "font-weight: bold;" +
                    "font-size: 20px" +
                    "}" +
                    ".text-gray{" +
                    "color: gray;" +
                    "font-weight: bold;" +
                    "font-size:20px" +
                    "}" +
                    "a {" +
                    "  background-color: #5356FF;" +
                    "  border: none;" +
                    "  padding: 15px 30px;" +
                    "  font-size: 16px;" +
                    "  border-radius: 5px;" +
                    "  cursor: pointer;" +
                    "  color: white !important;" +
                    "  text-decoration: none;" +
                    "  margin-top: 10px;" +
                    "  display: inline-block;" +
                    "  transition: background-color 0.3s ease;" +
                    "}" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class=\"container\">" +
                    "<span class='text-principal'>Tech</span><span class='text-gray'>Temple</span>" +
                    "<h2>Reset Your Password</h2>" +
                    "<p>Here's the link for your password reset:</p>" +
                    "<a href='http://localhost:4200/reset-password?token="+token + "&id="+user.get().getId()+ "'>" +
                        "Reset Password" +
                    "</a>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
            message.setContent(bodyHtml,"text/html; charset=utf-8");
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            sender.send(message);
        }
        }
        public void resetPassword(String token, Long id, String password) {
            if (resetTokens.get(id) != null) {
                if (resetTokens.get(id).equals(token)){
                    usersService.updatePassword(id, password);
                    resetTokens.remove(id);
                }
            }
        }
    }
