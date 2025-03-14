package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.userService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class userController {
    private final userService userService;

    public userController(userService userService) {
        this.userService = userService;
    }

    // CRUD user
    // @GetMapping("/user/create")
    // public String getUser() {
    // User user = new User();
    // user.setName("Phach");
    // user.setEmail("Thephach");
    // user.setPassword("123434");
    // this.userService.handleCreateUser(user);
    // return "create user";
    // }
    @PostMapping("/create")
    public String createUser(@RequestBody User posrManUser) {
        // TODO: process POST request

        this.userService.handleCreateUser(posrManUser);
        return "create user";
    }

    // Hàm show users
    @GetMapping("/users")
    public List<User> getUsers() {
        List<User> Users = this.userService.getAll();
        return Users;
    }

    // PathVariable: check thông tin user
    @GetMapping("/user/{id}")
    public User userById(@PathVariable("id") long id) {
        return this.userService.fetchUserById(id);
    }

    // Update user
    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {
        // TODO: process PUT request
        User currentUser = this.userService.handleUpdateUser(user);
        return currentUser;
    }

}
