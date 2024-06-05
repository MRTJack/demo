package uz.pdp.app_auditing_project.hr_management.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import uz.pdp.app_auditing_project.hr_management.models.User;
import uz.pdp.app_auditing_project.hr_management.models.enums.RoleName;
import uz.pdp.app_auditing_project.hr_management.payload.ApiResponse;
import uz.pdp.app_auditing_project.hr_management.payload.LoginDTO;
import uz.pdp.app_auditing_project.hr_management.payload.RegisterDTO;
import uz.pdp.app_auditing_project.hr_management.repository.RoleRepository;
import uz.pdp.app_auditing_project.hr_management.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    public ApiResponse registerUser(RegisterDTO registerDTO) {
        boolean b = userRepository.existsByEmail(registerDTO.getEmail());
        if (b) {
            return new ApiResponse("This email has been registered for a long time", false);
        }
        User user = new User();
        user.setFirst_name(registerDTO.getFirst_name());
        user.setLast_name(registerDTO.getLast_name());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR)));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);

        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("Account is success registered", true);
    }


    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("alizanovmarat1@gmail.com");
            helper.setTo(sendingEmail);
            helper.setSubject("Verify your account");




            Context context = new Context();
            context.setVariable("emailCode", emailCode);
            context.setVariable("sendingEmail", sendingEmail);

            String emailContent = templateEngine.process("verify-email", context);

            helper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }

    public ApiResponse verify_Email(String email, String emailCode) {
        System.out.println("Checking emailCode: " + emailCode + " with email: " + emailCode);

        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(emailCode, email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Email account is success", true);
        }
        return new ApiResponse("Email account is not success", false);
    }

    public ApiResponse login(LoginDTO loginDTO) {

        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }
}
