package com.example.votenow.repository;

import com.example.votenow.entity.Vorschlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VorschlagRepository extends JpaRepository<Vorschlag, Long> {

    List<Vorschlag> findByFrageId(Long frageId);
}
