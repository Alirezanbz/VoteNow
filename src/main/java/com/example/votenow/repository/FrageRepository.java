package com.example.votenow.repository;

import com.example.votenow.entity.Frage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FrageRepository extends CrudRepository<Frage, Long> {

    @Query("SELECT f FROM Frage f JOIN f.vorschlaege v JOIN v.antworten a WHERE a.user.id = :userId OR a.userHash = :userHash")
    List<Frage> findVotedFragenByUserOrHash(@Param("userId") Long userId, @Param("userHash") String userHash);
}
