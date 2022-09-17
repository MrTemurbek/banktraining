package uz.banktraining.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.entity.Participants;
import uz.banktraining.service.ParticipantService;


@RestController
@RequestMapping("/api")
public class ParticipantController {
    private final ParticipantService service;

    public ParticipantController(ParticipantService service) {
        this.service = service;
    }

    @GetMapping("/getAll")
    public ResponseDTO getAll(){
        return new ResponseDTO(0, "SUCCESS", null, service.getAll());
    }

    @GetMapping("/save")
    public ResponseDTO save(@RequestBody Participants participants){
        service.save(participants);
        return new ResponseDTO(0, "SUCCESS", null, null);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable("id") String id) {
        return service.downloadFile(id);
    }


}
