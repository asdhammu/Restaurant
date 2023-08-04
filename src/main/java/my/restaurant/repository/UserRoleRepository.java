package my.restaurant.repository;

import my.restaurant.dto.UserRole;
import my.restaurant.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(UserRole userRole);
}
