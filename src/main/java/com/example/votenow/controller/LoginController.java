package com.example.votenow.controller;

import com.example.votenow.entity.ResetPasswordToken;
import com.example.votenow.entity.User;
import com.example.votenow.repository.ResetPasswordTokenRepository;
import com.example.votenow.repository.UserRepository;
import com.example.votenow.service.MailSenderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class LoginController {

    private final UserRepository userRepository;

    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    private final MailSenderService mailSender;

    ResetPasswordToken resetPasswordToken;

    public LoginController(UserRepository userRepository, ResetPasswordTokenRepository resetPasswordTokenRepository, MailSenderService mailSender) {
        this.userRepository = userRepository;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.mailSender = mailSender;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }


    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpServletRequest request) {
        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            // Store user in session after successful login
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", user);

            model.addAttribute("user", user.getId());
            return "redirect:/home";
        } else {
            model.addAttribute("email",email);
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }


    @GetMapping("/login/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("email", "");
        return "forgot-password";
    }

    @PostMapping("/login/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) throws Exception {
        User user = userRepository.findByEmail(email);

        if (user != null) {

            String token = mailSender.generateToken();
            LocalDateTime expiryDate = LocalDateTime.now().plusDays(2);

            ResetPasswordToken existingToken = resetPasswordTokenRepository.findByUser(user);
            if (existingToken != null) {

                resetPasswordTokenRepository.delete(existingToken);
            }

            ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
            resetPasswordToken.setUser(user);
            resetPasswordToken.setToken(token);
            resetPasswordToken.setExpireDate(expiryDate);

            resetPasswordTokenRepository.save(resetPasswordToken);


            mailSender.sendResetPasswordEmail(email,token);
            model.addAttribute("message", "A reset password email has been sent to your email address.");
        } else {

            model.addAttribute("error", "No account found with that email address.");
        }

        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        resetPasswordToken = resetPasswordTokenRepository.findByToken(token);

        if (resetPasswordToken != null) {

            model.addAttribute("token", token);
            return "reset-password";
        } else {
            // User does not exist
            model.addAttribute("error", "No account found with that email address.");
            return "error";
        }
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token, @RequestParam("password") String password, Model model) {

        resetPasswordToken = resetPasswordTokenRepository.findByToken(token);

        if (resetPasswordToken != null) {

            User user = resetPasswordToken.getUser();
            user.setPassword(password);
            userRepository.save(user);
            resetPasswordTokenRepository.delete(resetPasswordToken);
            model.addAttribute("message", "Your password has been updated.");
            return "login";
        } else {

            model.addAttribute("error", "No account found with that email address.");
            return "error";
        }
    }


}
