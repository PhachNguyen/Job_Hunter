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
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
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

    public User handlerGetUserbyUserName(String username) {
        return this.userRepository.findByEmail(username);
    }

    // Hàm tìm kiếm email:
    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    // Hàm để trả dữ liệu từ response
    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        // ResCreateUserDTO.CompanyUser com = new ResCreateUserDTO.CompanyUser();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        // if (user.getCompany() != null) {
        // com.setId(user.getCompany().getId());
        // com.setName(user.getCompany().getName());
        // res.setCompany(com);
        // }
        return res;
    }
    // Response user cho server :
    // public ResUserDTO convertToResUserDTO(User user) {
    // ResUserDTO res = new ResUserDTO();
    // // ResUserDTO.CompanyUser com = new ResUserDTO.CompanyUser();
    // // ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();
    // // if (user.getCompany() != null) {
    // // com.setId(user.getCompany().getId());
    // // com.setName(user.getCompany().getName());
    // // res.setCompany(com);
    // // }

    // if (user.getRole() != null) {
    // roleUser.setId(user.getRole().getId());
    // roleUser.setName(user.getRole().getName());
    // res.setRole(roleUser);
    // }

    // res.setId(user.getId());
    // res.setEmail(user.getEmail());
    // res.setName(user.getName());
    // res.setAge(user.getAge());
    // res.setUpdatedAt(user.getUpdatedAt());
    // res.setCreatedAt(user.getCreatedAt());
    // res.setGender(user.getGender());
    // res.setAddress(user.getAddress());
    // return res;
    // }

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

    // Hàm Update Refresh_Token :
    public void updateUserToken(String token, String email) {
        User currentUser = this.handlerGetUserbyUserName(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
    // Hàm check tt user
    public User getUserByRefreshTokenAndEmail(String token, String email){
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
