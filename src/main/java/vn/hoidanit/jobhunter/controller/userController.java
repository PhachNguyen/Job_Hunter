package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.userService;
import vn.hoidanit.jobhunter.service.error.idInvalidException;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class userController {
    private final userService userService;

    public userController(userService userService) {
        this.userService = userService;
    }

    // @ResponseEntity : Trả về phản hồi cho người dùng
    // CRUD user
    // @GetMapping("/users/create")
    // public String getUser() {
    // User user = new User();
    // user.setName("Phach");
    // user.setEmail("Thephach");
    // user.setPassword("123434");
    // this.userService.handleCreateUser(user);
    // return "create user";
    // }
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User posrManUser) {
        // TODO: process POST request
        this.userService.handleCreateUser(posrManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    // Hàm show users
    @GetMapping("/users")
    public List<User> getUsers() {
        List<User> Users = this.userService.getAll();
        return Users;
    }

    // PathVariable: check thông tin user
    @GetMapping("/users/{id}")
    public User userById(@PathVariable("id") long id) {
        return this.userService.fetchUserById(id);
    }

    // Update user
    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        // TODO: process PUT request
        User currentUser = this.userService.handleUpdateUser(user);
        return currentUser;
    }

    // Hàm delete
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws idInvalidException {
        if (id >= 1500) {
            throw new idInvalidException("ID không lớn hơn 1500");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
