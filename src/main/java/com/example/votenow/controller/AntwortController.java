package com.example.votenow.controller;

import com.example.votenow.entity.Antwort;
import com.example.votenow.entity.User;
import com.example.votenow.entity.Vorschlag;
import com.example.votenow.service.AntwortService;
import com.example.votenow.service.VorschlagService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AntwortController {
    @Autowired
    AntwortService antwortService;
    @Autowired
    VorschlagService vorschlagService;

    @GetMapping("/antwort/{id}")
    public String getAntwort(Model model, @PathVariable Long id) {
        model.addAttribute("antwort", antwortService.getAntwort(id));
        return "antwort";
    }

    @PostMapping("/save-ratings")
    public String saveRatings(@RequestParam Map<String, String> ratings, HttpServletRequest request, Model model) {
        boolean hasUserAlreadyRated = false;
        for (Map.Entry<String, String> entry : ratings.entrySet()) {
            String[] parts = entry.getKey().split("_");

            if (parts.length < 2) {
                continue;
            }

            Long vorschlagId = Long.valueOf(parts[1]);
            Integer rating = Integer.valueOf(entry.getValue());
            if (saveRatingToDatabase(rating, vorschlagId, request)) {
                hasUserAlreadyRated = true;
            }
        }

        if (hasUserAlreadyRated) {
            model.addAttribute("alreadyRated", true);
            model.addAttribute("message", "You have already rated this Vorschlag.");
            return "already-rated-message";
        } else {
            model.addAttribute("message", "You rated successfully.");
            return "already-rated-message";
        }
    }

    private boolean saveRatingToDatabase(Integer rating, Long vorschlagId, HttpServletRequest request) {
        System.out.println("Rating: " + rating);

        Vorschlag vorschlag = vorschlagService.getVorschlag(vorschlagId);


        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedInUser");

        if(antwortService.hasUserAlreadyRated(vorschlag, user)) {

            System.out.println("User has already rated this vorschlag");
            return true;
        }


        Antwort antwort = new Antwort(rating);
        antwort.setVorschlag(vorschlag);
        antwort.setUser(user);  // set the user
        antwortService.createAntwort(vorschlagId, antwort);
        return false;

    }


}
