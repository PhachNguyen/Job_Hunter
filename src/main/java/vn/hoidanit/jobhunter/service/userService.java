package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.userRepository;

@Service
public class userService {

    private final userRepository userRepository;

    public userService(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    // Hàm xem All Users
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    // Hàm fetch by ID
    public User fetchUserById(long id) {
        return this.userRepository.findById(id);
    }

    // Hàm update User
    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setName(reqUser.getName());
            currentUser.setEmail(reqUser.getEmail());
            currentUser.setPassword(reqUser.getPassword());
            // Update
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    // Hàm delete user
    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
        ;
    }

    // Hàm tìm kiếm email
    public User handlerGetUserbyUserName(String username) {
        return this.userRepository.findByEmail(username);
    }
}
