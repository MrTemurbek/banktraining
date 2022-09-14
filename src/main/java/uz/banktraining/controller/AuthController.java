package uz.banktraining.controller;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.banktraining.dto.AdminDTO;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.service.AdminService;
import uz.banktraining.service.ItemService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AdminService adminService;
    private final ItemService itemService;


    public AuthController(AdminService adminService, ItemService itemService) {

        this.adminService = adminService;
        this.itemService = itemService;
    }

    @PostMapping("/check")
    public ResponseDTO checkAdmin(@RequestBody AdminDTO admin) {
        return adminService.check(admin);
    }


    @PostMapping("/upload")
    public ResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            itemService.save(file);
            return new ResponseDTO(0, "SUCCESS", null, null);
        } catch (Exception e) {
            return new ResponseDTO(1, "ERROR", e.getMessage(), null);
        }
    }
}
