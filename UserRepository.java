package uz.pdp.app_auditing_project.hr_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.app_auditing_project.hr_management.models.User;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);

    Optional<User> findByEmail(String username);
}
