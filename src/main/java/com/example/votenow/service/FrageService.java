package com.example.votenow.service;

import com.example.votenow.entity.Frage;

import java.util.List;

public interface FrageService {
    public Frage createFrage(Frage frage);
    public Frage getFrage(Long id);
    public Frage updateFrage(Frage frage);
    public void deleteFrage(Long id);
    public List<Frage> getFragen();
}
