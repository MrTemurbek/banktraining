package uz.banktraining.service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import uz.banktraining.dto.AdminDTO;
import uz.banktraining.dto.ResponseDTO;
import uz.banktraining.util.JWTUtil;

@Service
public class AdminService {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public AdminService(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public ResponseDTO check(AdminDTO admin ){
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(admin.getUsername(),
                        admin.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        }catch (BadCredentialsException e){
            return new ResponseDTO(1, "ERROR", "Login or Password is not correct", null);
        }
        String token = jwtUtil.generateToken(admin.getUsername());
        return new  ResponseDTO(0, "SUCCESS", null, token);
    }

}
