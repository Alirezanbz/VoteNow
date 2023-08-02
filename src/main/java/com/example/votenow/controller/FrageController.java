package com.example.votenow.controller;

import com.example.votenow.entity.Frage;
import com.example.votenow.entity.User;
import com.example.votenow.entity.Vorschlag;
import com.example.votenow.service.AuswertungService;
import com.example.votenow.service.FrageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@Controller
public class FrageController {
    @Autowired
    FrageService frageService;

    @Autowired
    AuswertungService auswertungService;


    @GetMapping("/frage")
    public String getFrage(Model model, Long frageId) {

        model.addAttribute("frage", frageService.getFrage(frageId));
        return "frage";
    }

    @GetMapping("/home")
    public String getFragen(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("userId", user.getId());
        model.addAttribute("fragen", frageService.getFragen());
        return "home";
    }

    @PostMapping("/handlecreatefrage")
    public String createFrage(Frage frage, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        frageService.createFrage(frage, user.getId());
        return "redirect:/home";
    }

    @GetMapping("/createfrage")
    public String createFrageForm(Model model) {
        Frage frage = new Frage();
        model.addAttribute("frage", frage);
        return "createfrage";
    }

    @PostMapping("/toggleFrage/{frageId}")
    public String toggleFrage(@PathVariable Long frageId, HttpServletRequest request){

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedInUser");

        Frage frage = frageService.getFrage(frageId);
        if(frage.getUser().getId() == user.getId()){
            frage.setIsActive(!frage.getIsActive());
            frageService.updateFrage(frage);
        }
        return "redirect:/home";
    }
    @GetMapping("/auswertung")
    public String getAuswertung(Model model, @RequestParam Long frageId) {
        List<Map.Entry<Vorschlag, Double>> averageRatings = auswertungService.getAverageRatings(frageId);
        Map<Vorschlag, AuswertungService.RatingColor> vorschlagColor = auswertungService.getVorschlagColor(frageId);

        model.addAttribute("averageRatings", averageRatings);
        model.addAttribute("vorschlagColor", vorschlagColor);
        return "auswertung";
    }
    @GetMapping("/vorschlagdetails")
    public String getVorschlagDetails(Model model,@RequestParam Long vorschlagId){

        Map<User, Integer> userRatings = auswertungService.getUserRatings(vorschlagId);
        model.addAttribute("userRatings", userRatings);
        return "vorschlagDetails";

    }



}
