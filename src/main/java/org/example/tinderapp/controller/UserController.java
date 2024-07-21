package org.example.tinderapp.controller;

import org.example.tinderapp.domain.entity.Login;
import org.example.tinderapp.domain.entity.User;
import org.example.tinderapp.exception.UserNotFoundException;
import org.example.tinderapp.service.UserService;
import org.example.tinderapp.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("currentDate", LocalDate.now());
        return "index";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("login", new Login());
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) throws SQLException, UserNotFoundException {
        userService.register(user.getName(), user.getSurname(), user.getUserName(), user.getEmail(), user.getPhoneNumber(), user.getPassword());

        // Automatically log in the user after registration
        Login login = new Login();
        login.setUsername(user.getEmail());  // assuming email is used as username
        login.setPassword(user.getPassword());

        boolean isAuthenticated = userService.login(login.getUsername(), login.getPassword());
        if (isAuthenticated) {
            Utils.userId = userService.getUserId(login.getUsername(), login.getPassword());
            model.addAttribute("message", "Giriş uğurludur");
            return "redirect:/profiles";  // redirect to the profiles page after login
        } else {
            model.addAttribute("message", "Giriş uğursuz oldu");
            return "register";
        }
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Login login, Model model) throws SQLException, UserNotFoundException {
        boolean isAuthenticated = userService.login(login.getUsername(), login.getPassword());
        if (isAuthenticated) {
            Utils.userId = userService.getUserId(login.getUsername(), login.getPassword());
            model.addAttribute("message", "Giriş uğurludur");
            return "redirect:/profiles";
        } else {
            model.addAttribute("message", "İstifadəçi məlumatları yanlışdır");
            return "login";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, Model model) throws SQLException, UserNotFoundException {
        userService.deleteUser(id);
        model.addAttribute("message", "İstifadəçi uğurla silindi");
        return "index";
    }

    @GetMapping("/getAll")
    public String getAllUsers(Model model) throws SQLException, UserNotFoundException {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "people-list";
    }
}
