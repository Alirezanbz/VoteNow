package com.example.votenow.controller;
import com.example.votenow.entity.User;
import com.example.votenow.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/verification")
public class VerificationController {

    private final UserRepository userRepository;




    public VerificationController( UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @GetMapping
    public String showVerificationForm(Model model) {
        model.addAttribute("verificationCode", "");
        return "verification";
    }

    @RequestMapping( method = RequestMethod.POST)
    public String sendVerificationCode(@SessionAttribute("registration") User user, @RequestParam("verificationCode") String verificationCode, Model model) throws Exception {
        String vc = RegController.verificationCode;
        if (verificationCode.equals(vc)) {

            model.addAttribute("success", true);


            user.setEmail(user.getEmail());
            user.setName(user.getName());
            user.setPassword(user.getPassword());
            user.setStatus(true);
            userRepository.save(user);




        } else {

            model.addAttribute("errorMessage", "Invalid verification code. Please try again.");
        }
        return "verification";
    }

}
