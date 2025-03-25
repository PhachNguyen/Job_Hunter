package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.repository.companyRepo;
import vn.hoidanit.jobhunter.repository.userRepository;

@Service
public class companyService {

    private final userRepository userRepository;
    private final companyRepo companyRepo;

    public companyService(companyRepo companyRepo,
            userRepository userRepository) {
        this.companyRepo = companyRepo;
        this.userRepository = userRepository;
    }

    public Company handleCreateCompany(Company c) {
        return this.companyRepo.save(c);
    }

    // Hàm render Company :
    public List<Company> getAll() {
        return this.companyRepo.findAll();
    }

    // Hàm Update thông tin :
    public Company handleUpdateCompany(Company c) {
        Optional<Company> companyOptional = this.companyRepo.findById(c.getId());
        if (companyOptional.isPresent()) {
            Company currentCompany = companyOptional.get();
            currentCompany.setLogo(c.getLogo());
            currentCompany.setName(c.getName());
            currentCompany.setDescription(c.getDescription());
            currentCompany.setAddress(c.getAddress());
            return this.companyRepo.save(currentCompany);
        }
        return null;
    }

    // public ResultPaginationDTO handleGetCompany(Specification<Company> spec,
    // Pageable pageable) {
    // Page<Company> pCompany = this.companyRepository.findAll(spec, pageable);
    // ResultPaginationDTO rs = new ResultPaginationDTO();
    // ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

    // mt.setPage(pageable.getPageNumber() + 1);
    // mt.setPageSize(pageable.getPageSize());

    // mt.setPages(pCompany.getTotalPages());
    // mt.setTotal(pCompany.getTotalElements());

    // rs.setMeta(mt);
    // rs.setResult(pCompany.getContent());
    // return rs;
    // }
    // Hàm deleted Company
    public void handleDeleteCompany(long id) {
        Optional<Company> comOptional = this.companyRepo.findById(id);
        // if (comOptional.isPresent()) {
        // Company com = comOptional.get();
        // // fetch all user belong to this company
        // List<User> users = this.userRepository.findByCompany(com);
        // this.userRepository.deleteAll(users);
        // }

        this.companyRepo.deleteById(id);
    }

    public Optional<Company> findById(long id) {
        return this.companyRepo.findById(id);
    }
}
