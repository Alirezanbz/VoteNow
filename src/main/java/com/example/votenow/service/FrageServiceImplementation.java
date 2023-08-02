package com.example.votenow.service;

import com.example.votenow.entity.Frage;
import com.example.votenow.entity.User;
import com.example.votenow.exception.FrageNotFoundException;
import com.example.votenow.repository.FrageRepository;
import com.example.votenow.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    @Override
    public Frage createFrage(Frage frage, int userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));
        frage.setUser(user);
        frage.setIsActive(true);
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


        calendar.add(Calendar.MONTH, 1);


        Date antwortDeadline = calendar.getTime();

        frage.setAntwortDeadline(antwortDeadline);
    }

    private void updateFrageStatus(Frage frage) {
        if(new Date().after(frage.getAntwortDeadline())) {
            frage.setIsActive(false);
            frageRepository.save(frage);
        }
    }
}
