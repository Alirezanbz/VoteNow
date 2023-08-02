    package com.example.votenow.service;

    import com.example.votenow.entity.User;
    import com.example.votenow.repository.AntwortRepository;
    import com.example.votenow.entity.Antwort;
    import com.example.votenow.entity.Vorschlag;
    import com.example.votenow.exception.AntwortNotFoundException;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;
    @Service
    public class AntwortServiceImplementation implements AntwortService{
        @Autowired
        AntwortRepository antwortRepository;
        @Autowired
        VorschlagService vorschlagService;

        @Override
        public Object getAntwort(Long id) {
            Optional<Antwort> antwort = antwortRepository.findById(id);
            if (antwort.isPresent()) {
                return antwort.get();
            }else throw new AntwortNotFoundException(id);
        }

        @Override
        public Antwort createAntwort(Long vorschlagId, Antwort antwort) {
            Vorschlag vorschlag = vorschlagService.getVorschlag(vorschlagId);
            vorschlag.getAntworten().add(antwort);
            return antwortRepository.save(antwort);
        }

        @Override
        public void deleteAntwort(Long id) {
            Antwort antwort = (Antwort) getAntwort(id);
            antwortRepository.delete(antwort);
        }

        @Override
        public List<Antwort> getAntworten(Long vorschlagId) {
            vorschlagService.getVorschlag(vorschlagId); // presence check
            return antwortRepository.findAllByVorschlagId(vorschlagId);
        }

        @Override
        public Antwort updateAntwort(Antwort newAntwort) {
            Optional<Antwort> existingAntwort = antwortRepository.findById(newAntwort.getId());
            if (existingAntwort.isPresent()) {
                return antwortRepository.save(newAntwort);
            } else throw new AntwortNotFoundException(newAntwort.getId());
        }
        @Override
        public boolean hasUserAlreadyRated(Vorschlag vorschlag, User user){
            List<Antwort> existingAntwort = antwortRepository.findByVorschlagAndUser(vorschlag, user);
            return !existingAntwort.isEmpty();

        }


    }
