package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.resultPaginationDTO;
import vn.hoidanit.jobhunter.service.userService;

import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.idInvalidException;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class userController {
    private final userService userService;
    private final PasswordEncoder passwordEncoder;

    public userController(userService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
    @ApiMessage("Create a new user ")
    public ResponseEntity<User> createUser(@Valid @RequestBody User postManUser) throws idInvalidException {
        // TODO: process POST request
        // Mã hóa mk khi truyền vào database.
        boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
        if (isEmailExist) {
            throw new idInvalidException(
                    "Email: " + postManUser.getEmail() + " đã tồn tại vui lòng sử dụng email khác");
        }
        postManUser.setPassword(passwordEncoder.encode(postManUser.getPassword()));
        this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    // Hàm show users
    @GetMapping("/users")
    // @CrossOrigin(origins = "http://localhost:3000")
    @ApiMessage("Fetch all users")
    public ResponseEntity<resultPaginationDTO> getUsers(
            @Filter Specification<User> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(spec, pageable));
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
    @ApiMessage("Delete user by id")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws idInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new idInvalidException("Không tồn tại user có ID: " + currentUser.getId());
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
