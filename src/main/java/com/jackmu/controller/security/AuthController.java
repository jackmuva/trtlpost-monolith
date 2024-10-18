package com.jackmu.controller.security;

import com.jackmu.model.security.*;
import com.jackmu.service.security.AuthEmailService;
import com.jackmu.service.security.AuthService;
import com.jackmu.service.security.PasswordResetService;
import com.jackmu.util.GenericHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;
    private PasswordResetService passwordResetService;
    private AuthEmailService emailService;
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    public AuthController(AuthService authService, PasswordResetService passwordResetService, AuthEmailService emailService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
        this.emailService = emailService;
    }

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDTO loginDTO){
        String token = authService.login(loginDTO);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) throws Exception{
        return authService.register(registerDTO);
    }

    @PostMapping(value = {"/resetPassword"})
    public ResponseEntity<String> resetPassword(@RequestParam("email") String userEmail) throws Exception {
        User user = passwordResetService.loadUserByEmail(userEmail);
        if (user == null) {
            throw new Exception();
        }
        String token = UUID.randomUUID().toString();
        passwordResetService.createPasswordResetTokenForUser(user, token);
        emailService.sendResetToken(token, user);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @PostMapping("/savePassword")
    public GenericHttpResponse savePassword(@RequestBody PasswordDto passwordDto) {
        String result = passwordResetService.validatePasswordResetToken(passwordDto.getToken());

        if(result != null) {
            return new GenericHttpResponse(HttpStatus.NOT_ACCEPTABLE.value(), result);
        }

        Optional<User> user = passwordResetService.getUserByPasswordResetToken(passwordDto.getToken());
        if(user.isPresent()) {
            passwordResetService.changeUserPassword(user.get(), passwordDto.getNewPassword());
            return new GenericHttpResponse(HttpStatus.OK.value(), "Password Reset Successful");
        } else {
            return new GenericHttpResponse(HttpStatus.NOT_ACCEPTABLE.value(), "User not found");
        }
    }
}
