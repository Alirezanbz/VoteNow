package com.example.votenow.repository;

import com.example.votenow.entity.ResetPasswordToken;
import com.example.votenow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer> {


    @Query("select t from ResetPasswordToken t where t.token = :token")
    ResetPasswordToken findByToken(@Param("token") String token);


    ResetPasswordToken findByUser(User user);



}
