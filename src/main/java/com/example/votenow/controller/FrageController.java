package com.example.votenow.controller;

import com.example.votenow.entity.Frage;
import com.example.votenow.entity.User;
import com.example.votenow.service.FrageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FrageController {
    @Autowired
    FrageService frageService;

    @GetMapping("/frage")
    public String getFrage(Model model, Long frageId) {

        model.addAttribute("frage", frageService.getFrage(frageId));
        return "frage";
    }

    @GetMapping("/home")
    public String getFragen(Model model, Integer userId) {
        System.out.println(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("fragen", frageService.getFragen());
        return "home";
    }

    @PostMapping("/handlecreatefrage")
    public String createFrage(Frage frage) {
        frageService.createFrage(frage);
        return "redirect:/home";
    }

    @GetMapping("/createfrage")
    public String createFrageForm(Model model) {
        Frage frage = new Frage();
        model.addAttribute("frage", frage);
        return "createfrage";
    }
}
