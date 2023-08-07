package com.example.votenow.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "antwort")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Antwort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "wert")
    @NonNull
    private Integer wert;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private User user;


    @ManyToOne(optional = false)
    @JoinColumn(name = "vorschlagID", referencedColumnName = "id")
    private Vorschlag vorschlag;

    @Column(name = "anonymous", nullable = false)
    private Boolean anonymous = false;

    private String userHash;


}
