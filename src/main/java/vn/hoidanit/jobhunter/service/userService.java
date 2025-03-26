package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public resultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        // Page<User> pageUser = this.userRepository.findAll(pageable); // Gọi db và
        // phân trang
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        resultPaginationDTO rs = new resultPaginationDTO(); // Tạo object DTO
        meta mt = new meta(); // tạo metadata

        mt.setPage(pageable.getPageNumber() + 1); // Số trang hiện tại và
        // PageRequest.of() //bắt đầu từ 0
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages()); // Số lượng phần tử trên mỗi trang
        mt.setTotal(pageUser.getTotalElements()); // Tổng số trang

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent()); // Trả về ds của trang hiện tại
        return rs;
    }
}
