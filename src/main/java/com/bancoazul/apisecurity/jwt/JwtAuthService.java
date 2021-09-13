package com.bancoazul.apisecurity.jwt;

import com.bancoazul.apisecurity.rsa.RSAKeyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RSAKeyProperties rsaKeyProp;

    public String login(String username, String password) {
        // User authentication
        Authentication authentication = null;
        try {
            // This method will call us erDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("User authentication failed");
        }
        JwtUser loginUser = (JwtUser) authentication.getPrincipal();
        // Generating Token
        return jwtTokenUtil.generateToken(loginUser, rsaKeyProp.getPrivateKey());
    }
}