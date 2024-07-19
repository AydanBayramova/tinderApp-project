package org.example.tinderapp.controller;

import org.example.tinderapp.domain.entity.Login;
import org.example.tinderapp.domain.entity.User;
import org.example.tinderapp.exception.UserNotFoundException;
import org.example.tinderapp.service.UserService;
import org.example.tinderapp.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) throws SQLException {
        User registeruser = userService.register(user.getName(), user.getSurname(), user.getUserName(), user.getEmail(), user.getPhoneNumber(), user.getPassword());

        Map<String, Object> map = new HashMap<>();
        map.put("Message", "Qeydiyyat uğurla başa çatdı");
        return ResponseEntity.ok(map);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) throws SQLException, UserNotFoundException {
        String username = login.getUsername();
        String password = login.getPassword();

        boolean isAuthenticated = userService.login(username, password);
        if (isAuthenticated) {
            Utils.userId = userService.getUserId(login.getUsername(), login.getPassword());
            return ResponseEntity.ok("Giriş uğurludur");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("İstifadəçi məlumatları yanlışdır");
        }
    }

    @DeleteMapping("/delete/id")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws SQLException, UserNotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.ok("İstifadəçi uğurla silindi");
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() throws SQLException, UserNotFoundException {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
