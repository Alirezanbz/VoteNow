package com.example.votenow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Entity
@Table(name ="user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



    @Column(unique = true)
    @Pattern(regexp=".+@gmail\\.com$", message="Email should be in the format of name@gmail.com")
    private String email;
    private String name;
    private String password;

    @Column(name = "verified")
    private boolean status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Frage> fragen;



}
