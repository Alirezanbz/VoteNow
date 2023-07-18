package com.example.votenow.repository;

import com.example.votenow.entity.Vorschlag;
import org.springframework.data.repository.CrudRepository;

public interface VorschlagRepository extends CrudRepository<Vorschlag, Long> {
}
