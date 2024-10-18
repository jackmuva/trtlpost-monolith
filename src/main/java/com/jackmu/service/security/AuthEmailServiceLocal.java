package com.jackmu.service.security;

import com.jackmu.model.security.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@Profile("local-profile")
public class AuthEmailServiceLocal implements AuthEmailService {

    private static final String PASSWORD_RESET_ENDPOINT = "https://localhost:3000/changePassword/";
    private static final Logger LOGGER = Logger.getLogger(AuthEmailServiceAws.class.getName());

    public void sendResetToken(String token, User user) {
        LOGGER.info("url: " + PASSWORD_RESET_ENDPOINT + token);
    }
}

