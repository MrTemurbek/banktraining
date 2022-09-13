package uz.banktraining.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.banktraining.dto.AdminDTO;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.service.AdminService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AdminService adminService;

    public AuthController( AdminService adminService) {

        this.adminService = adminService;
    }

    @PostMapping("/check")
    public ResponseDTO checkAdmin(@RequestBody AdminDTO admin) {
        return adminService.check(admin);
    }


}
