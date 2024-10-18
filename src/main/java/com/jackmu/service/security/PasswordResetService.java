package com.jackmu.service.security;

import com.jackmu.model.security.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface PasswordResetService {
    User loadUserByEmail(String email) throws UsernameNotFoundException;
    void createPasswordResetTokenForUser(User user, String token);
    String validatePasswordResetToken(String token);
    void changeUserPassword(User user, String password);
    Optional<User> getUserByPasswordResetToken(String token);
}
