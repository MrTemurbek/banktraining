package uz.banktraining.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.banktraining.dto.AdminDTO;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.service.AdminService;
import uz.banktraining.service.ExcelService;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AdminService adminService;
    private final ExcelService excelService;


    public AuthController(AdminService adminService, ExcelService excelService) {

        this.adminService = adminService;
        this.excelService = excelService;

    }

    @PostMapping("/check")
    public ResponseDTO checkAdmin(@RequestBody AdminDTO admin) {
        return adminService.check(admin);
    }


    @PostMapping("/upload")
    public ResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            excelService.save(file);
            return new ResponseDTO(0, "SUCCESS", null, null);
        } catch (Exception e) {
            return new ResponseDTO(1, "ERROR", e.getMessage(), null);
        }
    }
}
