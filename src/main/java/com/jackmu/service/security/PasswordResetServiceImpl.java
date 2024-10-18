package com.jackmu.service.security;

import com.jackmu.model.security.PasswordResetToken;
import com.jackmu.model.security.User;
import com.jackmu.repository.security.PasswordResetTokenRepository;
import com.jackmu.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class PasswordResetServiceImpl implements PasswordResetService{
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private UserRepository userRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(email, email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "Token is invalid"
                : isTokenExpired(passToken) ? "Token is expired"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token) .getUser());
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanTokenRepository(){
        passwordResetTokenRepository.deleteAllExpired();
    }
}
