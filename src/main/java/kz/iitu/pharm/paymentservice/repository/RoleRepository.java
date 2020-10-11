package kz.iitu.pharm.paymentservice.repository;

import kz.iitu.pharm.paymentservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role findByName(String role);
}
