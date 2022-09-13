package uz.banktraining.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.banktraining.repo.AdminRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    ObjectMapper objectMapper;

    public MyUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        if (adminRepository.findByUsername(username)==null  ){
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(adminRepository.findByUsername(username));
    }
}
