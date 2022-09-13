package uz.banktraining.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.banktraining.dto.AdminDTO;


@Repository
public interface AdminRepository extends JpaRepository<AdminDTO, Long> {
    AdminDTO findByUsername(String username);
}
