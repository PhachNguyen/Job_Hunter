package vn.hoidanit.jobhunter.domain;

import java.time.Instant;

import org.apache.catalina.security.SecurityUtil;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "companies")


public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "name không được để trống")
    private String name;

    @Column(length = 4000)// Dùng String sẽ bị limit 255 character and Oracle k hỗ trợ ColumDefinication="MEDIUMTEXT"
    private String description;

    private String address;

    private String logo;
    // Thuộc Java Time API dùng để đại diện cho một thời điểm chính xác theo UTC
    // khác với kiểu DATE là có thể tính toán đc thời gian còn DATE bắt buộc phải
    // dùng Caledar
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GTM +7")
    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = vn.hoidanit.jobhunter.util.SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? vn.hoidanit.jobhunter.util.SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

}
