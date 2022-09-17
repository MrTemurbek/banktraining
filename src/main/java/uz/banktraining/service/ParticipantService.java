package uz.banktraining.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.banktraining.entity.Participants;
import uz.banktraining.repo.ParticipantsRepository;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    public void update(Participants participant){

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
