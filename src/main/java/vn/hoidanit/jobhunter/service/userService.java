package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.meta;
import vn.hoidanit.jobhunter.domain.dto.resultPaginationDTO;
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
    public List<User> getAll(Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(pageable);
        return pageUser.getContent();
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

    // Phân trang :
    // Pageable : Đại diện cho thông tin về phân trang và sắp xếp dữ liệu. Lấy dữ
    // liệu theo từng trang
    public resultPaginationDTO fetchAllUser(Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(pageable);
        resultPaginationDTO rs = new resultPaginationDTO();
        meta mt = new meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());
        return rs;
    }
}
