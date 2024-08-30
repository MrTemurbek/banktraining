package uz.banktraining.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import uz.banktraining.dto.ParticipantDTO;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.entity.Participants;
import uz.banktraining.pdf.PDFHelper;
import uz.banktraining.repo.ParticipantsRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ParticipantService {

    private final ParticipantsRepository repository;
    private final SMSService smsService;
    private static final String PATH_TO_SAVE = "pdf/certificate_";
    private static final String LINK = "banktraining.uz/userCertificates/";

    public ParticipantService(ParticipantsRepository repository, SMSService smsService) {
        this.repository = repository;
        this.smsService = smsService;
    }

    public Page<Participants> getAllByPage(int pageNumber) {
        if (repository.findAll(PageRequest.of(pageNumber - 1, 15, Sort.by("certificateID"))).isEmpty()) {
            return null;
        }
        return repository.findAll(PageRequest.of(pageNumber - 1, 15, Sort.by("certificateID")));
    }

    public List<Participants> getAll() {
        return repository.findAll();
    }

    public ResponseDTO save(ParticipantDTO dto) {
        if (repository.existsByCertificateID(dto.getCertificateID())) {
            return new ResponseDTO(1, "ERROR", "Bu id bilan sertifikat yaratilgan, iltimos boshqa id kiriting", null);
        }
        try {
            Participants participants = new Participants(dto);
            participants.setPath(PATH_TO_SAVE + participants.getCertificateID());
            participants.setPath(participants.getCertificateID());
            participants.setLink("http://" + LINK + participants.getCertificateID());
            participants.setCreatedAt(new Date());
            repository.save(participants);
            smsService.sendSMS(participants.getCertificateID());
            new PDFHelper().pdfCreator(participants.getName(), participants.getCertificateID(), participants.getLink());
            return new ResponseDTO(0, "SUCCESS", null, null);
        } catch (Exception e) {
            return new ResponseDTO(1, "ERROR", e.getMessage(), null);
        }
    }

    @GetMapping("/searchById")
    public ResponseDTO searchById(String id) {
        if (repository.existsByCertificateID(id)) {
            return new ResponseDTO(0, "SUCCESS", null, repository.getParticipantsByCertificateID(id));
        } else return new ResponseDTO(1, "ERROR", "This CertificateID is not found!", null);
    }

    public Participants getByID(String id) {
        return repository.getParticipantsByCertificateID(id);
    }

    @Transactional
    public ResponseDTO update(String certificateId, Participants participant) {
        if (!Objects.equals(certificateId, participant.getCertificateID())) {
            return new ResponseDTO(1, "ERROR", "Certificate Ids are not same!", null);
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            Participants participantDto = repository.getParticipantsByCertificateID(certificateId);
            String link = participantDto.getLink();
            participantDto = mapper.convertValue(participant, Participants.class);
            repository.updateParticipants(participant.getName(), participant.getNumber(), participant.getCertificateID());
            smsService.sendSMS(certificateId);
            new PDFHelper().pdfCreator(participantDto.getName(), participantDto.getCertificateID(), link);
        } catch (Exception e) {
            return new ResponseDTO(1, "ERROR", e.getMessage(), null);
        }
        return new ResponseDTO(0, "SUCCESS", null, null);
    }


    @Transactional
    public ResponseDTO delete(String id) {
        try {
            Participants participant = repository.getParticipantsByCertificateID(id);
            deleteFiles(participant.getPath());
            repository.deleteById(participant.getId());
        } catch (Exception e) {
            return new ResponseDTO(1, "ERROR", e.getMessage(), null);

        }
        return new ResponseDTO(0, "SUCCESS", null, null);


    }

    public ResponseEntity<?> downloadFile(String id) {
        String PATH = "pdf/";
        Path path = Paths.get(PATH + "certificate_" + id + ".pdf");

        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert resource != null;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    public ResponseDTO deleteAll() {
        try {
            List<Participants> participantsList = repository.findAll();
            for (Participants participants : participantsList) {
                deleteFiles(participants.getPath());
            }

            repository.deleteAll();
            return new ResponseDTO(0, "SUCCESS", null, null);
        } catch (Exception e) {
            return new ResponseDTO(1, "ERROR", e.getMessage(), null);
        }
    }

    public void deleteFiles(String filePath) {
        Path path = Paths.get(filePath + ".pdf");

        try {
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            System.out.printf("No such file: %s\n", path);
        } catch (DirectoryNotEmptyException ex) {
            System.out.printf("Directory %s is not empty\n", path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



    public ResponseDTO getAllLinks() {
        List<String> links = repository.getAllLinks();
        return new ResponseDTO(0, "SUCCESS", null, links);
    }
}


