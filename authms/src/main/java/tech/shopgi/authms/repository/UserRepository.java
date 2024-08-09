package tech.shopgi.authms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.shopgi.authms.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
