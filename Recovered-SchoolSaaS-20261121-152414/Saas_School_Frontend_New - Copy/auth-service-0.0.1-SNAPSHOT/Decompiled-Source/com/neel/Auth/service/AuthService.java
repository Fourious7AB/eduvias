/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Auth.exception.AlreadyExistException
 *  com.neel.Auth.model.AuthUser
 *  com.neel.Auth.model.PasswordResetToken
 *  com.neel.Auth.repo.AuthUserRepository
 *  com.neel.Auth.service.AuthService
 *  jakarta.annotation.PostConstruct
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 *  org.springframework.stereotype.Service
 */
package com.neel.Auth.service;

import com.neel.Auth.exception.AlreadyExistException;
import com.neel.Auth.model.AuthUser;
import com.neel.Auth.model.PasswordResetToken;
import com.neel.Auth.repo.AuthUserRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthUserRepository authUserRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private Map<String, PasswordResetToken> passwordResetTokens = new HashMap();

    public String registerUser(AuthUser authUser) {
        if (this.authUserRepository.existsByUsername(authUser.getUsername())) {
            throw new AlreadyExistException("Username already exists! with role:" + authUser.getRole());
        }
        authUser.setPassword(this.passwordEncoder.encode((CharSequence)authUser.getPassword()));
        this.authUserRepository.save((Object)authUser);
        return "User registered successfully!";
    }

    public AuthUser updateUserByUsername(String username, AuthUser updatedUser) {
        AuthUser existingUser = (AuthUser)this.authUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(this.passwordEncoder.encode((CharSequence)updatedUser.getPassword()));
        }
        if (updatedUser.getMessage() != null) {
            existingUser.setMessage(updatedUser.getMessage());
        }
        return (AuthUser)this.authUserRepository.save((Object)existingUser);
    }

    public Optional<AuthUser> deleteUserByUsername(String username) {
        Optional user = this.authUserRepository.findByUsername(username);
        if (user.isPresent()) {
            this.authUserRepository.deleteByUsername(username);
        }
        return user;
    }

    public Optional<AuthUser> validateUser(String username, String password) {
        AuthUser authUser;
        Optional user = this.authUserRepository.findByUsername(username);
        if (user.isPresent() && this.passwordEncoder.matches((CharSequence)password, (authUser = (AuthUser)user.get()).getPassword())) {
            return Optional.of(authUser);
        }
        return Optional.empty();
    }

    @PostConstruct
    public void initAdminUser() {
        if (!this.authUserRepository.existsByUsername("vais")) {
            AuthUser adminUser = new AuthUser();
            adminUser.setUsername("admin");
            adminUser.setPassword(this.passwordEncoder.encode((CharSequence)"admin123"));
            adminUser.setRole("vais");
            this.authUserRepository.save((Object)adminUser);
            System.out.println("Default admin user created with username: admin and password: admin123");
        }
    }

    public boolean isEmailRegistered(String email) {
        return this.authUserRepository.findByUsername(email).isPresent();
    }

    public void updatePassword(String email, String newPassword) {
        AuthUser user = (AuthUser)this.authUserRepository.findByUsername(email).orElseThrow(() -> new RuntimeException("User not found for email: " + email));
        String hashedPassword = this.hashPassword(newPassword);
        user.setPassword(hashedPassword);
        this.authUserRepository.save((Object)user);
    }

    private String hashPassword(String password) {
        return password;
    }

    public boolean isPasswordStrong(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$");
    }

    public String generatePasswordResetToken(String email) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpirationTime(LocalDateTime.now().plusHours(1L));
        this.passwordResetTokens.put(token, resetToken);
        System.out.println("Generated password reset token: " + token + " for email: " + email);
        return token;
    }
}

