package uz.banktraining.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.entity.Participants;
import uz.banktraining.service.ParticipantService;

import java.util.List;

@RestController
public class ParticipantController {
    private final ParticipantService service;

    public ParticipantController(ParticipantService service) {
        this.service = service;
    }

    @GetMapping("/getAll")
    public ResponseDTO getAll(){
        return new ResponseDTO(0, "SUCCESS", null, service.getAll());
    }

    @GetMapping("save")
    public ResponseDTO save(@RequestBody Participants participants){
        service.save(participants);
        return new ResponseDTO(0, "SUCCESS", null, null);
    }


}
