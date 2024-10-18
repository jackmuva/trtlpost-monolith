package com.jackmu.repository.security;

import com.jackmu.model.security.PasswordResetToken;
import com.jackmu.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

    List<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

    @Modifying
    @Query(value = "delete from password_reset_token t where t.expiry_date <= NOW()", nativeQuery = true)
    @Transactional
    void deleteAllExpired();
}
