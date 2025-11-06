package ru.lipnin.itmohomework.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lipnin.itmohomework.security.entity.UserRole;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByRoleType(UserRole.RoleType roleType);
}
