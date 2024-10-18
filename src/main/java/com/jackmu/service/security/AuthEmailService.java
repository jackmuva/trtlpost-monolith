package com.jackmu.service.security;

import com.jackmu.model.security.User;

public interface AuthEmailService {
    void sendResetToken(String token, User user);
}
