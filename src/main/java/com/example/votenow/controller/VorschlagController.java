package com.example.votenow.controller;

import com.example.votenow.entity.Frage;
import com.example.votenow.entity.Vorschlag;
import com.example.votenow.service.FrageService;
import com.example.votenow.service.VorschlagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VorschlagController {
    @Autowired
    VorschlagService vorschlagService;
    @Autowired
    FrageService frageService;

    @GetMapping("/createvorschlag")
    public String getCreateVorschlagForm(Model model, Long frageId) {
        Vorschlag vorschlag = new Vorschlag();
        Frage frage = frageService.getFrage(frageId);

        model.addAttribute("vorschlag", vorschlag);
        model.addAttribute("frage", frage);

        return "createvorschlag";
    }

    @PostMapping("/handlecreatevorschlag")
    public String createVorschlag(Vorschlag vorschlag, Long frageId, RedirectAttributes redirectAttributes) {
        vorschlagService.saveVorschlag(frageId, vorschlag);
        redirectAttributes.addAttribute("frageId", frageId);

        return "redirect:/frage";
    }
}
