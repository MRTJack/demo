package uz.pdp.app_auditing_project.hr_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.app_auditing_project.hr_management.payload.ApiResponse;
import uz.pdp.app_auditing_project.hr_management.payload.LoginDTO;
import uz.pdp.app_auditing_project.hr_management.payload.RegisterDTO;
import uz.pdp.app_auditing_project.hr_management.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        ApiResponse apiResponse = authService.registerUser(registerDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/verify_Email")
    public HttpEntity<?> verifyEmail(@RequestParam String email, @RequestParam String emailCode) {
        System.out.println("Received emailCode: " + emailCode + " and email: " + email);

        ApiResponse apiResponse = authService.verify_Email(emailCode, email);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDTO loginDTO) {
        ApiResponse apiResponse = authService.login(loginDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }

}
