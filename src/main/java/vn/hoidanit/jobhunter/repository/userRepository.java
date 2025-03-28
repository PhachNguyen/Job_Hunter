package vn.hoidanit.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.User;

@Repository
public interface userRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // Xem thông tin user
    List<User> findAll();

    // Lấy user theo id
    User findById(long id);

    User findByEmail(String email);
}
