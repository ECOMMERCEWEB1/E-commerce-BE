package com.webproject.ecommerce.controllers;

import com.webproject.ecommerce.dto.MessageDTO;
import com.webproject.ecommerce.dto.UserDTO;
import com.webproject.ecommerce.mappers.UserMapper;
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

    private final UserMapper userMapper;

    public UsersController(UsersService usersService, UserMapper userMapper) {
        this.userMapper = userMapper;
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
                    body(userMapper.toDto(user,
                            "Email already exists !"));
        else {
            User created_user = usersService.createUser(user);
            return ResponseEntity.
                    created(new URI("/api/users")).
                    body(userMapper.toDto(created_user,
                            "User created successfully !"));
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "id") Long id, @Valid @RequestBody User user) {

        if (!usersService.userIdExists(id))
            return ResponseEntity.
                    badRequest().
                    body(userMapper.toDto(user,"ID does not exist !"));

        else if (!usersService.userIdCheck(user, id))
            return ResponseEntity.
                    status(HttpStatus.NOT_FOUND).
                    body(userMapper.toDto(user,
                            "IDs must match !"));
        else {
            User updated_user = usersService.updateUser(user.getFirstName(), user.getLastName(), user.getAge(), user.getEmail(), id);
            return ResponseEntity.ok().body(userMapper.toDto(updated_user, "User updated successfully !"));
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