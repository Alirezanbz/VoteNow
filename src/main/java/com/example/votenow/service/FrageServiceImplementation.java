package com.example.votenow.service;

import com.example.votenow.entity.Frage;
import com.example.votenow.exception.FrageNotFoundException;
import com.example.votenow.repository.FrageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FrageServiceImplementation implements FrageService{
    @Autowired
    FrageRepository frageRepository;

    @Override
    public Frage createFrage(Frage frage) {
        setAntwortDeadline(frage);
        return (Frage) frageRepository.save(frage);
    }

    @Override
    public Frage getFrage(Long id) {
        Optional<Frage> frage = frageRepository.findById(id);
        if (frage.isPresent()) {
            return frage.get();
        } else throw new FrageNotFoundException(id);
    }

    @Override
    public Frage updateFrage(Frage frage) {
        Optional<Frage> existingFrage = frageRepository.findById(frage.getId());
        if (existingFrage.isPresent()) {
            return (Frage) frageRepository.save(frage);
        } else throw new FrageNotFoundException(frage.getId());
    }

    @Override
    public void deleteFrage(Long id) {
        Optional<Frage> exsistingFrage = frageRepository.findById(id);
        if (exsistingFrage.isPresent()){
            frageRepository.delete(exsistingFrage.get());
        }
    }

    @Override
    public List<Frage> getFragen() {
        return (List<Frage>) frageRepository.findAll();
    }

    public void setAntwortDeadline(Frage frage) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(frage.getVorschlagDeadline());

// Add one month to the Calendar instance
        calendar.add(Calendar.MONTH, 1);

// Get the resulting date as a Date object
        Date antwortDeadline = calendar.getTime();

// Set the antwortDeadline value in your object
        frage.setAntwortDeadline(antwortDeadline);
    }
}
