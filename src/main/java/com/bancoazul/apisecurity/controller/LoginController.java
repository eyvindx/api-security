package com.bancoazul.apisecurity.controller;

import com.bancoazul.apisecurity.jwt.JwtAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class LoginController {

    @Autowired
    private JwtAuthService jwtAuthService;

    /**
     * Login method
     * Easy to test, front end does not use JSON data format
     * @param username user name
     * @param password password
     * @return result
     */
    @PostMapping({"/login", "/"})
    public ResponseEntity<?> login(String username, String password) {
        String token = jwtAuthService.login(username, password);
        //return ResultGenerator.success("Login successful", token);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
