package tech.shopgi.authms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import tech.shopgi.authms.model.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    UserDetails findByUsername(String username);
}
