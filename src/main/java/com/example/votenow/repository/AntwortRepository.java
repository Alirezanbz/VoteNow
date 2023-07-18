package com.example.votenow.repository;

import com.example.votenow.entity.Antwort;
import com.example.votenow.entity.User;
import com.example.votenow.entity.Vorschlag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AntwortRepository extends CrudRepository<Antwort, Long> {
    public List<Antwort> findAllByVorschlagId(Long vorschlagId);
    List<Antwort> findByVorschlagAndUser(Vorschlag vorschlag, User user);
}
