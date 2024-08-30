package uz.banktraining.controller;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.banktraining.dto.ParticipantDTO;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.entity.Participants;
import uz.banktraining.service.ExcelService;
import uz.banktraining.service.ParticipantService;
import uz.banktraining.service.SMSService;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class ParticipantController {
    private final ParticipantService service;
    private final ExcelService excelService;
    private final SMSService smsService;


    public ParticipantController(ParticipantService service, ExcelService excelService, SMSService smsService) {
        this.service = service;
        this.excelService = excelService;
        this.smsService = smsService;
    }
    @GetMapping("/getAll/{page}")
    public ResponseDTO getAllByPage(@PathVariable(name ="page", required=false) @DefaultValue(value = "1") Integer page){
        return new ResponseDTO(0, "SUCCESS", null, service.getAllByPage(page));
    }


    @GetMapping("/searchById/{id}")
    public ResponseDTO searchById(@PathVariable("id") String id){
        return service.searchById(id);
    }


    @PostMapping("/save")
    public ResponseDTO save(@RequestBody ParticipantDTO participants){
        return service.save(participants);

    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable("id") String id) {
        return service.downloadFile(id);
    }


    @PostMapping("/renameFiles")
    public ResponseDTO renameCertificateName(@RequestParam("file") MultipartFile file) {
            return excelService.renameFile(file);
    }


    @GetMapping("/getById/{id}")
    public Participants getByCertificateId(@PathVariable("id") String id){
        return service.getByID(id);
    }



    @PutMapping("/update/{certificateId}")
public ResponseDTO update(@PathVariable String certificateId, @RequestBody Participants participant){
        return service.update(certificateId, participant);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseDTO delete(@PathVariable("id") Object id){
         return service.delete(String.valueOf(id));
    }


    @PostMapping("/sms/{certificateId}")
    public ResponseDTO sendSMS(@PathVariable("certificateId") Object certificateId){
        return smsService.sendSMS(String.valueOf(certificateId));
    }



    @PostMapping("/upload")
    public ResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
        excelService.save(file);
        return new ResponseDTO(0, "SUCCESS", null, null);
    }


    @DeleteMapping("/deleteAll")
    public ResponseDTO deleteAll() {
        service.deleteAll();
        return new ResponseDTO(0, "SUCCESS", null, null);
    }


    @GetMapping("/getAll")
    public ResponseDTO getAll(){
        return new ResponseDTO(0, "SUCCESS", null, service.getAll());
    }

}
