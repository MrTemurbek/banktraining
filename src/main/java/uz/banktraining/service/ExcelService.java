package uz.banktraining.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.entity.Participants;
import uz.banktraining.excel.ExcelHelper;
import uz.banktraining.pdf.PDFHelper;
import uz.banktraining.repo.ParticipantsRepository;
import uz.banktraining.util.DeleteAllInFolder;

import java.util.ArrayList;
import java.util.LinkedList;
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

    public ResponseDTO renameFile(MultipartFile file){
        try {
            List<String> participantsIDs = ExcelHelper.excelGetIds(file.getInputStream());
            List<Participants> infoThatIds = new LinkedList<>();
            for (String ids: participantsIDs) {
                infoThatIds.add(repository.getParticipantsByCertificateID(ids));
            }
            new DeleteAllInFolder().deleteInFolder();  //clear folder
            for (Participants participants : infoThatIds){
                new PDFHelper().changeNameAndLocation(participants.getCertificateID(), participants.getName(), participants.getPath());
            }
        }catch (Exception e){
            System.err.println("Error");
            return new ResponseDTO(1, "ERROR", e.getMessage(), null);
        }

        return new ResponseDTO(0, "SUCCESS", null, null);
    }
}
