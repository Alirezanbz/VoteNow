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

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vorschlagID", referencedColumnName = "id")
    private Vorschlag vorschlag;
}
