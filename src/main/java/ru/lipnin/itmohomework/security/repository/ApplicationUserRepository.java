package ru.lipnin.itmohomework.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;
import ru.lipnin.itmohomework.security.entity.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    List<ApplicationUser> findByUserRoleAndRemovedFalse(UserRole role);

    Set<ApplicationUser> findAllByIdInAndRemovedFalse(List<Long> listId);

    Optional<ApplicationUser> findByUsername(String name);

    boolean existsByUsername(String username);
}
