package com.example.votenow.service;

import com.example.votenow.entity.Frage;
import com.example.votenow.entity.Vorschlag;
import com.example.votenow.exception.FrageNotFoundException;
import com.example.votenow.exception.VorschlagNotFoundException;
import com.example.votenow.repository.FrageRepository;
import com.example.votenow.repository.VorschlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VorschlagServiceImplementation implements VorschlagService {
    @Autowired
    VorschlagRepository vorschlagRepository;
    @Autowired
    FrageRepository frageRepository;

    @Override
    public Vorschlag getVorschlag(Long vorschlagId) {
        Optional<Vorschlag> option = vorschlagRepository.findById(vorschlagId);
        if (option.isPresent()) {
            return option.get();
        } else throw new VorschlagNotFoundException(vorschlagId);
    }

    @Override
    public Vorschlag saveVorschlag(Long frageID, Vorschlag vorschlag) {
        Optional<Frage> frage = frageRepository.findById(frageID);
        if (frage.isPresent()) {
            frage.get().getVorschlaege().add(vorschlag);
            vorschlag.setFrage(frage.get());
            return vorschlagRepository.save(vorschlag);
        } else throw new FrageNotFoundException(frageID);
    }

    @Override
    public List<Vorschlag> getVorschlaege(Long frageID) {
        Optional<Frage> frage = frageRepository.findById(frageID);
        if (frage.isPresent()) {
            return frage.get().getVorschlaege();
        } else throw new FrageNotFoundException(frageID);
    }

    @Override
    public void deleteVorschlag(Long frageId, Long vorschlagId) {
        Optional<Frage> frage = frageRepository.findById(frageId);
        Optional<Vorschlag> vorschlag = vorschlagRepository.findById(vorschlagId);
        if (frage.isPresent()) {
            if (vorschlag.isPresent()) {
                vorschlagRepository.delete(vorschlag.get());
            } else throw new VorschlagNotFoundException(vorschlagId);
        } else throw new FrageNotFoundException(frageId);
    }

    @Override
    public Vorschlag updateVorschlag(Long frageId, Vorschlag vorschlag) {
        Optional<Frage> frage = frageRepository.findById(frageId);
        Optional<Vorschlag> existingVorschlag = vorschlagRepository.findById(vorschlag.getId());
        if (frage.isPresent()) {
            if (existingVorschlag.isPresent()) {
                return vorschlagRepository.save(vorschlag);
            } else throw new VorschlagNotFoundException(vorschlag.getId());
        } else throw new FrageNotFoundException(frageId);
    }

    @Override
    public List<Vorschlag> getAllVorschlaege() {
        return vorschlagRepository.findAll();
    }

    public List<Vorschlag> getVorschlaegeByFrageId(Long frageId) {

        return vorschlagRepository.findByFrageId(frageId);
    }
}
