package uz.banktraining.controller;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import uz.banktraining.dto.ParticipantDTO;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.entity.Participants;
import uz.banktraining.service.ExcelService;
import uz.banktraining.service.ParticipantService;


@RestController
@RequestMapping("/api")
public class ParticipantController {
    private final ParticipantService service;
    private final ExcelService excelService;


    public ParticipantController(ParticipantService service, ExcelService excelService) {
        this.service = service;
        this.excelService = excelService;
    }
    @CrossOrigin
    @GetMapping("/getAll/{page}")
    public ResponseDTO getAllByPage(@PathVariable(name ="page", required=false) @DefaultValue(value = "1") Integer page){
        return new ResponseDTO(0, "SUCCESS", null, service.getAllByPage(page));
    }

    @CrossOrigin
    @GetMapping("/searchById/{id}")
    public ResponseDTO searchById(@PathVariable("id") String id){
        return service.searchById(id);
    }

    @PostMapping("/save")
    public ResponseDTO save(@RequestBody ParticipantDTO participants){
        service.save(participants);
        return new ResponseDTO(0, "SUCCESS", null, null);
    }
    @CrossOrigin
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable("id") String id) {
        return service.downloadFile(id);
    }

    @CrossOrigin
    @GetMapping("/getById/{id}")
    public Participants getByCertificateId(@PathVariable("id") String id){
        return service.getByID(id);
    }


    @CrossOrigin
    @PutMapping("/update/{certificateId}")
public ResponseDTO update(@PathVariable String certificateId, @RequestBody Participants participant){
        return service.update(certificateId, participant);
    }


    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseDTO delete(@PathVariable("id") Object id){
         return service.delete(String.valueOf(id));

    }

    @CrossOrigin
    @PostMapping("/upload")
    public ResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            excelService.save(file);
            return new ResponseDTO(0, "SUCCESS", null, null);
        } catch (Exception e) {
            return new ResponseDTO(1, "ERROR", e.getMessage(), null);
        }
    }

    @CrossOrigin
    @DeleteMapping("/deleteAll")
    public ResponseDTO deleteAll() {
        try {
             service.deleteAll();
            return new ResponseDTO(0, "SUCCESS", null, null);
        } catch (Exception e) {
            return new ResponseDTO(1, "ERROR", e.getMessage(), null);
        }
    }

    @CrossOrigin
    @GetMapping("/getAll")
    public ResponseDTO getAll(){
        return new ResponseDTO(0, "SUCCESS", null, service.getAll());
    }

}
