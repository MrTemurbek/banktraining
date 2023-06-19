package uz.banktraining.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.banktraining.entity.Participants;
import uz.banktraining.excel.ExcelHelper;
import uz.banktraining.pdf.PDFHelper;
import uz.banktraining.repo.ParticipantsRepository;

import java.util.List;

@Service
public class ExcelService {

    private final ParticipantsRepository repository;
    private final SMSService smsService;

    public ExcelService(ParticipantsRepository repository, SMSService smsService) {
        this.repository = repository;
        this.smsService = smsService;
    }

    public void save(MultipartFile file)  {
        try {
        List<Participants> participantsList = ExcelHelper.excelToTutorials(file.getInputStream());
            participantsList.removeIf(participant -> repository.existsByCertificateID(participant.getCertificateID())
            );
            for (Participants participants : participantsList) {
                smsService.sendSMS(participants.getLink(), participants.getCertificateID());
                new PDFHelper().pdfCreator(participants.getName(), participants.getCertificateID(), participants.getLink());
            }
            try {
                repository.saveAll(participantsList);
            }
            catch (Exception e){
                System.err.println("Error while saving participants infos: " +e);
                throw new Exception("saveAll");
            }
    } catch (Exception e) {
            System.err.println("Error");
        throw new RuntimeException("fail to store excel data: " + e.getMessage());
    }
    }
}
