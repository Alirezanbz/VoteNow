package com.example.votenow.service;

import com.example.votenow.configuration.Pair;
import com.example.votenow.entity.Vorschlag;
import com.example.votenow.entity.Antwort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuswertungService {

    public enum RatingColor {
        RED, YELLOW, GREEN
    }

    @Autowired
    VorschlagService vorschlagService;

    @Autowired
    AntwortService antwortService;

    public List<Map.Entry<Vorschlag, Double>> getAverageRatings(Long frageId) {

        Map<Vorschlag, Double> averageRatings = new HashMap<>();
        Map<Vorschlag, Boolean> hasRedAntwort = new HashMap<>();
        List<Vorschlag> vorschlaege = vorschlagService.getVorschlaegeByFrageId(frageId);

        for (Vorschlag vorschlag : vorschlaege) {
            List<Antwort> antworten = antwortService.getAntworten(vorschlag.getId());
            OptionalDouble average = antworten.stream().mapToInt(Antwort::getWert).average();
            averageRatings.put(vorschlag, average.isPresent() ? average.getAsDouble() : 0.0);
            hasRedAntwort.put(vorschlag, antworten.stream().anyMatch(a -> a.getWert() >= 8));
        }


        return averageRatings.entrySet().stream()
                .sorted(Map.Entry.<Vorschlag, Double>comparingByKey(
                                Comparator.comparing(hasRedAntwort::get))
                        .thenComparing(Map.Entry::getValue))
                .collect(Collectors.toList());


    }
    public Map<Vorschlag, RatingColor> getVorschlagColor(Long frageId) {
        Map<Vorschlag, RatingColor> vorschlagColor = new HashMap<>();
        List<Vorschlag> vorschlaege = vorschlagService.getVorschlaegeByFrageId(frageId);

        for (Vorschlag vorschlag : vorschlaege) {
            List<Antwort> antworten = antwortService.getAntworten(vorschlag.getId());

            boolean hasRed = antworten.stream().anyMatch(a -> a.getWert() >= 8);
            boolean hasYellow = antworten.stream().anyMatch(a -> a.getWert() >= 3 && a.getWert() < 8);


            if (hasRed) {
                vorschlagColor.put(vorschlag, RatingColor.RED);
            } else if (hasYellow) {
                vorschlagColor.put(vorschlag, RatingColor.YELLOW);
            } else {
                vorschlagColor.put(vorschlag, RatingColor.GREEN);
            }
        }

        return vorschlagColor;
    }

    public List<Pair<String, Integer>> getUserRatings(Long vorschlagId){
        List<Pair<String, Integer>> userRatings = new ArrayList<>();
        Vorschlag vorschlag = vorschlagService.getVorschlag(vorschlagId);

        for (Antwort antwort : antwortService.getAntworten(vorschlag.getId())){
            if (antwort.getUser() == null) {
                userRatings.add(new Pair<>("Anonymous", antwort.getWert()));
            } else {
                userRatings.add(new Pair<>(antwort.getUser().getName(), antwort.getWert()));
            }
        }
        return userRatings;
    }




}


