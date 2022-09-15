package uz.banktraining.service;

import com.itextpdf.text.DocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.banktraining.entity.Participants;
import uz.banktraining.excel.ExcelHelper;
import uz.banktraining.pdf.PDFHelper;
import uz.banktraining.repo.ParticipantsRepository;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    private final ParticipantsRepository repository;

    public ExcelService(ParticipantsRepository repository) {
        this.repository = repository;
    }

    public void save(MultipartFile file)  {
        try {
        List<Participants> participantsList = ExcelHelper.excelToTutorials(file.getInputStream());
            for (Participants participant: participantsList) {
                int participantIndex = participantsList.indexOf(participant);
                if(repository.existsByCertificateID(participant.getCertificateID())){
                    participantsList.remove(participantIndex);
                }
            }
        repository.saveAll(participantsList);
    } catch (IOException e) {
        throw new RuntimeException("fail to store excel data: " + e.getMessage());
    }
    }
}
