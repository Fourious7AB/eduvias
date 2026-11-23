/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.neel.Auth.ApiResponseDto
 *  com.neel.Auth.controller.AuthController
 *  com.neel.Auth.model.AuthUser
 *  com.neel.Auth.model.PasswordResetToken
 *  com.neel.Auth.model.ResponseMessage
 *  com.neel.Auth.service.AuthService
 *  com.neel.Auth.service.EmailService
 *  com.neel.Auth.service.JwtTokenService
 *  jakarta.servlet.http.Cookie
 *  jakarta.servlet.http.HttpServletResponse
 *  jakarta.servlet.http.HttpSession
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpMethod
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.client.RestTemplate
 */
package com.neel.Auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neel.Auth.ApiResponseDto;
import com.neel.Auth.model.AuthUser;
import com.neel.Auth.model.PasswordResetToken;
import com.neel.Auth.model.ResponseMessage;
import com.neel.Auth.service.AuthService;
import com.neel.Auth.service.EmailService;
import com.neel.Auth.service.JwtTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value={"/api/auth"})
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private EmailService emailService;
    private final Map<String, PasswordResetToken> passwordResetTokens = new HashMap();

    @PostMapping(value={"/register"})
    public ResponseEntity<String> register(@RequestBody AuthUser authUser) {
        return ResponseEntity.ok((Object)this.authService.registerUser(authUser));
    }

    @PostMapping(value={"/login"})
    public ResponseEntity<?> login(@RequestBody AuthUser authUser, HttpServletResponse response) {
        return this.authService.validateUser(authUser.getUsername(), authUser.getPassword()).map(user -> {
            String token = this.jwtTokenService.generateToken(user);
            response.addHeader("Set-Cookie", "authToken=" + token + "; HttpOnly; Secure; Path=/; Max-Age=3600; SameSite=None");
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60);
            response.addCookie(cookie);
            HashMap<String, Object> responseBody = new HashMap<String, Object>();
            responseBody.put("message", "Login successful!");
            responseBody.put("role", user.getRole());
            if (user.getRole().equalsIgnoreCase("vais")) {
                responseBody.put("userDetails", Map.of("username", user.getUsername(), "role", user.getRole()));
                return ResponseEntity.ok(responseBody);
            }
            try {
                String userServiceUrl;
                RestTemplate restTemplate = new RestTemplate();
                if (user.getRole().equalsIgnoreCase("student")) {
                    userServiceUrl = "https://collage.vaisacademy.com/service1/admissions/number/" + authUser.getUsername();
                } else if (user.getRole().equalsIgnoreCase("teacher")) {
                    userServiceUrl = "https://collage.vaisacademy.com/service2/api/teachers/email/" + authUser.getUsername();
                } else if (user.getRole().equalsIgnoreCase("edp")) {
                    userServiceUrl = "https://collage.vaisacademy.com/service2/api/edp/email/" + authUser.getUsername();
                } else if (user.getRole().equalsIgnoreCase("accountant")) {
                    userServiceUrl = "https://collage.vaisacademy.com/service2/api/accounts/email/" + authUser.getUsername();
                } else if (user.getRole().equalsIgnoreCase("librarian")) {
                    userServiceUrl = "https://collage.vaisacademy.com/service2/api/lib/email/" + authUser.getUsername();
                } else if (user.getRole().equalsIgnoreCase("admin")) {
                    userServiceUrl = "https://collage.vaisacademy.com/service1/api/signups/email/" + authUser.getUsername();
                } else {
                    return ResponseEntity.status((HttpStatusCode)HttpStatus.UNAUTHORIZED).body((Object)"Unknown user role");
                }
                ResponseEntity userResponse = restTemplate.exchange(userServiceUrl, HttpMethod.GET, null, ResponseMessage.class, new Object[0]);
                if (userResponse.getStatusCode() == HttpStatus.OK) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Object userDetails = objectMapper.convertValue(((ResponseMessage)userResponse.getBody()).getData(), Object.class);
                    responseBody.put("userDetails", userDetails);
                    return ResponseEntity.ok(responseBody);
                }
                return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)"User details not found");
            }
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Error fetching user details");
            }
        }).orElse(ResponseEntity.status((int)401).body(Map.of("message", "Invalid username or password")));
    }

    @PostMapping(value={"/logout"})
    public ResponseEntity<?> logout(HttpServletResponse response, HttpSession session) {
        session.invalidate();
        String expiredCookie = "authToken=; HttpOnly; Secure; Path=/; Max-Age=0; SameSite=None";
        response.addHeader("Set-Cookie", expiredCookie);
        Cookie logoutCookie = new Cookie("authToken", null);
        logoutCookie.setHttpOnly(true);
        logoutCookie.setSecure(true);
        logoutCookie.setPath("/");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        Cookie jsessionCookie = new Cookie("JSESSIONID", null);
        jsessionCookie.setMaxAge(0);
        jsessionCookie.setPath("/");
        response.addCookie(jsessionCookie);
        HashMap<String, String> responseBody = new HashMap<String, String>();
        responseBody.put("message", "Logout successful!");
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(value={"/test"})
    public String testEndpoint() {
        return "Security is disabled!";
    }

    @PostMapping(value={"/forgot-password"})
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body((Object)"Username (email) is required.");
        }
        if (!this.authService.isEmailRegistered(username)) {
            return ResponseEntity.status((int)404).body((Object)"Email not found.");
        }
        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(15L);
        this.passwordResetTokens.put(resetToken, new PasswordResetToken(resetToken, username, expirationTime));
        String resetLink = "https://edu.vaisacademy.com/api/auth/reset-password/" + resetToken;
        this.emailService.sendEmail(username, "Password Reset Request", "Click the link to reset your password: " + resetLink);
        return ResponseEntity.ok((Object)"Password reset link sent to your email.");
    }

    @PostMapping(value={"/reset-password"})
    public ResponseEntity<?> resetPassword(@RequestParam(value="token") String token, @RequestBody Map<String, String> request) {
        String newPassword = request.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body((Object)"New password is required.");
        }
        PasswordResetToken tokenData = (PasswordResetToken)this.passwordResetTokens.get(token);
        System.out.println("Token data: " + tokenData);
        if (tokenData == null || tokenData.getExpirationTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status((int)401).body((Object)"Invalid or expired token.");
        }
        if (!this.authService.isPasswordStrong(newPassword)) {
            return ResponseEntity.badRequest().body((Object)"Password must meet complexity requirements.");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode((CharSequence)newPassword);
        this.authService.updatePassword(tokenData.getEmail(), hashedPassword);
        this.passwordResetTokens.remove(token);
        return ResponseEntity.ok((Object)"Password reset successful.");
    }

    @PutMapping(value={"/update/{username}"})
    public ResponseEntity<ApiResponseDto> updateUser(@PathVariable String username, @RequestBody AuthUser updatedUser) {
        try {
            AuthUser updated = this.authService.updateUserByUsername(username, updatedUser);
            return ResponseEntity.ok((Object)new ApiResponseDto(true, "User updated successfully", (Object)updated));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ApiResponseDto(false, e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/delete/{username}"})
    public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable String username) {
        Optional deleteUser = this.authService.deleteUserByUsername(username);
        if (deleteUser.isPresent()) {
            return ResponseEntity.ok((Object)new ApiResponseDto(true, "User deleted successfully", deleteUser.get()));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ApiResponseDto(false, "User not found", null));
    }

    @GetMapping(value={"/all"})
    public ResponseEntity<ResponseMessage> allUser() {
        List allUserData = this.authService.getAllUser();
        if (allUserData.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("No data available in db", null));
        }
        return ResponseEntity.ok((Object)new ResponseMessage("Data fetched successfully", (Object)allUserData));
    }

    @GetMapping(value={"/health"})
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok((Object)"Auth service is healthy");
    }
}

