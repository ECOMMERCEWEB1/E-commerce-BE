package com.webproject.ecommerce.controllers;

import com.webproject.ecommerce.dto.MessageDTO;
import com.webproject.ecommerce.dto.UserDTO;
import com.webproject.ecommerce.entities.Product;
import com.webproject.ecommerce.entities.User;
import com.webproject.ecommerce.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(usersService.getUsers());
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable(value = "id") Long id) {
        User user = usersService.getUserById(id);
        if (user != null)
            return ResponseEntity.ok().body(user);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("This user does not exist !"));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid User user) throws URISyntaxException {
        if (usersService.userEmailExists(user.getEmail()))
            return ResponseEntity.
                    internalServerError().
                    body(new UserDTO(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getAge(),
                            user.getEmail(),
                            user.getProducts(),
                            "Email already exists !"));
        else {
            User created_user = usersService.createUser(user);
            return ResponseEntity.
                    created(new URI("/api/users")).
                    body(new UserDTO(
                            created_user.getId(),
                            created_user.getFirstName(),
                            created_user.getLastName(),
                            created_user.getAge(),
                            created_user.getEmail(),
                            created_user.getProducts(),
                            "User created successfully !"));
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "id") Long id, @Valid @RequestBody User user) {

        if (!usersService.userIdExists(id))
            return ResponseEntity.
                    badRequest().
                    body(new UserDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAge(),
                    user.getEmail(),
                    user.getProducts(),"ID does not exist !"));

        else if (!usersService.userIdCheck(user, id))
            return ResponseEntity.
                    status(HttpStatus.NOT_FOUND).
                    body(new UserDTO(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getAge(),
                            user.getEmail(),
                            user.getProducts(),
                            "IDs must match !"));
        else {
            User updated_user = usersService.updateUser(user.getFirstName(), user.getLastName(), user.getAge(), user.getEmail(), id);
            return ResponseEntity.ok().body(new UserDTO(
                    updated_user.getId(),
                    updated_user.getFirstName(),
                    updated_user.getLastName(),
                    updated_user.getAge(),
                    updated_user.getEmail(),
                    updated_user.getProducts(), "User updated successfully !"));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long id) {
        if (usersService.userIdExists(id))
            usersService.deleteUser(id);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("User does not exist !"));
        return ResponseEntity.ok().body(new MessageDTO("User Deleted Successfully !"));
    }
}