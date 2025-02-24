package com.example.webtest.controller;

import com.example.webtest.model.HttpRequest;
import com.example.webtest.repository.HttpRequestRepository;
import com.example.webtest.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/http-request")
public class HttpRequestController {

    @Autowired
    private HttpRequestRepository httpRequestRepository;

    @Autowired
    private EmailService emailService;

    // Display login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Refers to login.html
    }

    // Handle login
    @PostMapping("/login")
    public String handleLoginRequest(@RequestParam String username,
                                     @RequestParam String password,
                                     HttpServletRequest request,
                                     Model model) {
        // Log details
        System.out.println("Login attempt for username: " + username);

        // Check for XSS and SQL Injection attacks BEFORE sanitizing input
        String attackType = detectAttack(username, password);

        // Now sanitize inputs
        username = sanitizeInput(username);
        password = sanitizeInput(password);

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String url = request.getRequestURL().toString();

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setIp(ip);
        httpRequest.setUserAgent(userAgent);
        httpRequest.setUrl(url);
        httpRequest.setTimestamp(LocalDateTime.now());
        httpRequest.setAttackType(attackType);

        // Log attack detection (for debugging)
        System.out.println("Detected attack type: " + attackType);

        // Save the request log regardless of attack detection
        httpRequestRepository.save(httpRequest);

        // Log and alert if attack detected
        if (!"None".equals(attackType)) {
            System.out.println("Attack detected: " + attackType);
            sendAlert(httpRequest);
            model.addAttribute("error", "Potential attack detected: " + attackType);
            return "login";
        }

        return "redirect:/http-request/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard"; // Refers to dashboard.html
    }

    private String sanitizeInput(String input) {
        if (input == null) return null;
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;");
    }

    private String detectAttack(String username, String password) {
        // Check for XSS first
        if (detectXSS(username) || detectXSS(password)) {
            return "XSS (Cross-Site Scripting)";
        }

        // Check for SQL Injection
        if (detectSQLInjection(username) || detectSQLInjection(password)) {
            return "SQL Injection";
        }

        return "None";
    }

    private boolean detectXSS(String input) {
        if (input == null || input.isEmpty()) return false;

        // XSS detection patterns (basic check for script tags, event handlers, etc.)
        String[] xssPatterns = {"<script>", "</script>", "onerror", "onload", "javascript:", "eval(", "<img", "<a"};
        for (String pattern : xssPatterns) {
            if (input.toLowerCase().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    private boolean detectSQLInjection(String input) {
        if (input == null || input.isEmpty()) return false;

        // SQL Injection detection patterns
        String[] sqlPatterns = {"select", "insert", "delete", "--", "'", "drop", "update", "union", "sleep", "benchmark"};
        for (String pattern : sqlPatterns) {
            if (input.toLowerCase().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    private void sendAlert(HttpRequest request) {
        String subject = "Alert: Attack Detected";
        String message = "Attack detected:\n" +
                "IP: " + request.getIp() + "\n" +
                "Time: " + request.getTimestamp() + "\n" +
                "Attack Type: " + request.getAttackType();
        emailService.sendEmail("skillfen203@gmail.com", subject, message);
    }
}
