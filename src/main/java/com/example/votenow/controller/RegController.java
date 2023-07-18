package com.example.votenow.controller;


import com.example.votenow.entity.User;
import com.example.votenow.repository.UserRepository;
import com.example.votenow.validation.GMailer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegController {

    private final UserRepository userRepository;

    private final GMailer gMailer;



    public static String verificationCode;

    public RegController(UserRepository userRepository, GMailer gMailer) {
        this.userRepository = userRepository;
        this.gMailer = gMailer;
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
        if(userRepository.existsByEmail(user.getEmail())) {
            mav.addObject("error", "Email is already in use.");
            return mav;
        }

        gMailer.sendVerificationEmail(user.getEmail());
        verificationCode = gMailer.getVerificationCode();
        try {
            user.setStatus(false);
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




}
