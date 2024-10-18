package com.jackmu.service.security;

import com.jackmu.model.security.LoginDTO;
import com.jackmu.model.security.RegisterDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    String login(LoginDTO loginDto);
    ResponseEntity<String> register(RegisterDTO registerDTO) throws Exception;
}
