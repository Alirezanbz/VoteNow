package com.example.votenow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "vorschlag")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Vorschlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "text")
    private String text;

    @NonNull
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name = "frageId", referencedColumnName = "id")
    private Frage frage;

    @JsonIgnore
    @OneToMany(mappedBy = "vorschlag", cascade = CascadeType.ALL)
    private List<Antwort> antworten;
}
