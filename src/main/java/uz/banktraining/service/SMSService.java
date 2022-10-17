package uz.banktraining.service;

import org.springframework.stereotype.Service;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.entity.Participants;
import uz.banktraining.repo.ParticipantsRepository;

@Service
public class SMSService {

    private final ParticipantsRepository repository;

    public SMSService(ParticipantsRepository repository) {
        this.repository = repository;
    }

    public ResponseDTO sendSMS(String id) {
        try {
            Participants participant = repository.getParticipantsByCertificateID(id);
            System.out.println("SMS -> LINK:" + participant.getLink() + "ID :"+id);
        }
        catch (Exception e){
            return new ResponseDTO(1, "ERROR", e.getMessage(), null);
        }
        return new ResponseDTO(0, "SUCCESS", null, null);
    }
}
