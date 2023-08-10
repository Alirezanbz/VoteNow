package com.example.votenow.controller;
import com.example.votenow.entity.User;
import com.example.votenow.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public String sendVerificationCode(@SessionAttribute("registration") User user, @RequestParam("verificationCode") String verificationCode, Model model, HttpServletRequest request) throws Exception {
        String vc = RegController.verificationCode;
        if (verificationCode.equals(vc)) {


            model.addAttribute("success", true);
            User existingUser = userRepository.findByEmail(user.getEmail());

            if(existingUser!=null && !existingUser.isStatus()){

                existingUser.setStatus(true);
                userRepository.save(existingUser);
                HttpSession session = request.getSession();
                session.setAttribute("loggedInUser", user);


                return "redirect:/home";

            }

        } else {

            model.addAttribute("errorMessage", "Invalid verification code. Please try again.");
        }
        return "verification";
    }

}
