package uz.banktraining.config;



import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.banktraining.service.MyUserDetailsService;
import uz.banktraining.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;


@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final MyUserDetailsService personDetailsService;

    public JWTFilter(JWTUtil jwtUtil, MyUserDetailsService personDetailsService) {
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException {
        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT Token in Bearer Header");
            } else {
                try {
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    UserDetails userDetails = personDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    userDetails.getPassword(),
                                    userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (Exception exc) {
                    HttpServletResponse httpResponse = (HttpServletResponse) httpServletResponse;
                    System.out.println("URL: "+httpServletRequest.getRequestURI());
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    httpResponse.getOutputStream().println("Token has expired");
                    httpResponse.getOutputStream().flush();
                    httpResponse.getOutputStream().close();
                }
            }
        }

        try{
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        catch (Exception e){
            HttpServletResponse httpResponse = (HttpServletResponse) httpServletResponse;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getOutputStream().println("Token is incorrect");
            httpResponse.getOutputStream().flush();
            httpResponse.getOutputStream().close();
            System.err.println("URL: "+httpServletRequest.getRequestURI()+ " \n error: "+e.getMessage());
            System.err.println("Access is denied, jwtFilter");
        }
    }
}
