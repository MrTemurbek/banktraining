package uz.banktraining.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.entity.Participants;
import uz.banktraining.pdf.PDFHelper;
import uz.banktraining.repo.ParticipantsRepository;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class ParticipantService {

    private final ParticipantsRepository repository;
    private final String PATH="./src/main/resources/pdf/";

    public ParticipantService(ParticipantsRepository repository) {
        this.repository = repository;
    }

    public List<Participants> getAll(){
        return repository.findAll();
    }

    public void save(Participants participants) {
        repository.save(participants);
    }

    public Participants getByID(String id){
        return repository.getParticipantsByCertificateID(id);
    }

    public ResponseDTO update(String certificateId, Participants participant){
        if(!Objects.equals(certificateId, participant.getCertificateID()))
        {
            return new ResponseDTO(1, "ERROR", "Certificate Ids are not same!", null);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            Participants participantDto = repository.getParticipantsByCertificateID(certificateId);
            String link = participantDto.getLink();
            participantDto = mapper.convertValue(participant, Participants.class);
            repository.updateParticipants(participant.getName(), participant.getSurname(), participant.getNumber(), participant.getCertificateDate(), participant.getCourse(), participant.getCertificateID());
            System.out.println(link);
            new PDFHelper().pdfCreator(participantDto.getName(), participantDto.getSurname(), participantDto.getCertificateID(), participantDto.getCertificateDate(), participantDto.getCourse(), link);
        } catch (Exception e) {
            return new ResponseDTO(1, "ERROR", e.getMessage(), null);
        }
        return new ResponseDTO(0, "SUCCESS", null, null);
    }

    public ResponseDTO delete(String id) {
        try{
            Participants participants = repository.getParticipantsByCertificateID(id);
            repository.deleteById(participants.getId());
            return new ResponseDTO(0, "SUCCESS", null, null);
        }
        catch (Exception e) {
            return new ResponseDTO(1, "ERROR", e.getMessage(), null);
        }

    }

    public ResponseEntity<?> downloadFile(String id) {
        Path path = Paths.get(PATH+ "certificate_" + id+".pdf");
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
