package uz.pdp.app_auditing_project.hr_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.app_auditing_project.hr_management.models.Role;
import uz.pdp.app_auditing_project.hr_management.models.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(RoleName roleName);
}
