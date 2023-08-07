package com.example.votenow.controller;

import com.example.votenow.entity.Antwort;
import com.example.votenow.entity.User;
import com.example.votenow.entity.Vorschlag;
import com.example.votenow.repository.UserRepository;
import com.example.votenow.service.AntwortService;
import com.example.votenow.service.VorschlagService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Map;

@Controller
public class AntwortController {
    @Autowired
    AntwortService antwortService;
    @Autowired
    VorschlagService vorschlagService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/antwort/{id}")
    public String getAntwort(Model model, @PathVariable Long id) {
        model.addAttribute("antwort", antwortService.getAntwort(id));
        return "antwort";
    }

    @PostMapping("/save-ratings")
    public String saveRatings(@RequestParam Map<String, String> ratings, HttpServletRequest request, Model model) {
        boolean hasUserAlreadyRated = false;
        boolean anonymousVote = "on".equalsIgnoreCase(ratings.get("anonymous")); // check if the anonymous checkbox is checked
        for (Map.Entry<String, String> entry : ratings.entrySet()) {
            String[] parts = entry.getKey().split("_");

            if (parts.length < 2) {
                continue;
            }

            if(parts[0].equalsIgnoreCase("anonymous")){
                continue;
            }


            Long vorschlagId = Long.valueOf(parts[1]);
            Integer rating = Integer.valueOf(entry.getValue());
            if (saveRatingToDatabase(rating, vorschlagId, request, anonymousVote)) {
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

    private boolean saveRatingToDatabase(Integer rating, Long vorschlagId, HttpServletRequest request, boolean anonymousVote) {
        System.out.println("Rating: " + rating);

        Vorschlag vorschlag = vorschlagService.getVorschlag(vorschlagId);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedInUser");
        String userHash = (String) session.getAttribute("userHash");

        if (userHash == null) {
            userHash = hashUserId(session.getId(), new Date());
            session.setAttribute("userHash", userHash);
        }


        Antwort antwort = new Antwort(rating);
        antwort.setAnonymous(anonymousVote);
        antwort.setVorschlag(vorschlag);

        antwort.setUserHash(userHash);

        if (!anonymousVote) {
            user = (User) session.getAttribute("loggedInUser");
            antwort.setUser(user);
        }


            // Check if the user has already voted
            if (antwortService.hasUserAlreadyRated(vorschlag, user, userHash, anonymousVote)) {
                System.out.println("User has already rated this vorschlag");
                return true;
            }



        antwortService.createAntwort(vorschlagId, antwort);
        return false;
    }

    private String hashUserId(String userId, Date date) {
        String originalString = userId + date.toString();
        return DigestUtils.sha256Hex(originalString);
    }




}
