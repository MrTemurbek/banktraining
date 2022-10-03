package uz.banktraining.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.banktraining.dto.AdminDTO;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.service.AdminService;
import uz.banktraining.service.ExcelService;
import uz.banktraining.service.ParticipantService;


@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    private final AdminService adminService;
    private final ParticipantService service;


    public AuthController(AdminService adminService, ParticipantService service) {
        this.adminService = adminService;
        this.service = service;
    }

    @PostMapping("/check")
    public ResponseDTO checkAdmin(@RequestBody AdminDTO admin) {
        return adminService.check(admin);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable("id") String id) {
        return service.downloadFile(id);
    }
}
