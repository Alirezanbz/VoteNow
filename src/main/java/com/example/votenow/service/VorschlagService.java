package com.example.votenow.service;

import com.example.votenow.entity.Vorschlag;

import java.util.List;

public interface VorschlagService {

    public List<Vorschlag> getVorschlaegeByFrageId(Long frageId);
    public Vorschlag getVorschlag(Long vorschlagId);
    public Vorschlag saveVorschlag(Long frageID, Vorschlag vorschlag);
    public List<Vorschlag> getVorschlaege(Long frageID);
    public void deleteVorschlag(Long frageId, Long vorschlagId);
    public Vorschlag updateVorschlag(Long frageId, Vorschlag vorschlag);

    List<Vorschlag> getAllVorschlaege();
}
