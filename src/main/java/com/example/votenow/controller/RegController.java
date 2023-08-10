package com.example.votenow.controller;


import com.example.votenow.entity.User;
import com.example.votenow.repository.UserRepository;
import com.example.votenow.service.MailSenderService;
import com.example.votenow.validation.GMailer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegController {

    private final UserRepository userRepository;

    private final MailSenderService mailSender;

    private final BCryptPasswordEncoder encoder;

    public static String verificationCode;

    public RegController(UserRepository userRepository, MailSenderService mailSender, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.encoder = encoder;
    }


    @RequestMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("registration");
        if(bindingResult.hasErrors()){
            return mav;

        }
        if (userRepository.existsByEmail(user.getEmail())) {
            User existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isStatus() == true) {
                mav.addObject("error", "Email is already in use.");
            } else {
                mailSender.sendVerificationEmail(user.getEmail());
                verificationCode = mailSender.getVerificationCode();
                HttpSession session = request.getSession();
                session.setAttribute("registration", user);
                mav.setViewName("verification");
                mav.addObject("registration", user);

            }
            return mav;
        }

        mailSender.sendVerificationEmail(user.getEmail());
        verificationCode = mailSender.getVerificationCode();
        try {
            user.setStatus(false);
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            mav.addObject("error", "Email is already in use.");
            return mav;
        }
        HttpSession session = request.getSession();
        session.setAttribute("registration", user);
        mav.setViewName("verification");
        mav.addObject("registration", user);
        return mav;
    }
    @RequestMapping(value = "/resend-verification", method = RequestMethod.GET)
    public String resendVerificationEmail(@RequestParam("email") String email, Model model) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user != null && !user.isStatus()) {
            mailSender.sendVerificationEmail(email);
            model.addAttribute("message", "Verification email sent successfully!");
            return "verification";
        } else {
            model.addAttribute("error", "Unable to resend verification email. Invalid request.");
            return "error";
        }
    }





}
