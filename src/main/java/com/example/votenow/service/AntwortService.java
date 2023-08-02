package com.example.votenow.service;

import com.example.votenow.entity.Antwort;
import com.example.votenow.entity.User;
import com.example.votenow.entity.Vorschlag;

import java.util.List;

public interface AntwortService {
    public Object getAntwort(Long id);
    public Antwort createAntwort(Long vorschlagId, Antwort antwort);

    public boolean hasUserAlreadyRated(Vorschlag vorschlag, User user);


    public void deleteAntwort(Long id);
    public List<Antwort> getAntworten(Long optionId);
    public Antwort updateAntwort(Antwort antwort);
}
