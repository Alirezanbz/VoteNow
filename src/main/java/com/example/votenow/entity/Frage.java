package com.example.votenow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "frage")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Frage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "title")
    private String title;

    @NonNull
    @Column(name = "vorschlag_deadline")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date vorschlagDeadline;

    @Column(name = "antwort_deadline")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date antwortDeadline;

    @NonNull
    @ManyToOne(optional = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "frage", cascade = CascadeType.ALL)
    private List<Vorschlag> vorschlaege;
}
