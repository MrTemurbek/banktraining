package uz.banktraining.service;

import org.springframework.stereotype.Service;
import uz.banktraining.entity.Participants;
import uz.banktraining.repo.ParticipantsRepository;

import java.util.List;

@Service
public class ParticipantService {

    private final ParticipantsRepository repository;


    public ParticipantService(ParticipantsRepository repository) {
        this.repository = repository;
    }

    public List<Participants> getAll(){
        return repository.findAll();
    }

    public void save(Participants participants) {
        repository.save(participants);
    }
}
