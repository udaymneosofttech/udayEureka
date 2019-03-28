package vio.live.swing.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import vio.live.swing.config.JwtConfig;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationfilter extends UsernamePasswordAuthenticationFilter{


    private AuthenticationManager authenticationManager;
    JwtConfig jwtConfig;

    public JwtAuthenticationfilter(AuthenticationManager authenticationManager,JwtConfig jwtConfig) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig=jwtConfig;

        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(),"POST"));

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse response) throws AuthenticationException {


        try {

            UserCredential userCredential =  new ObjectMapper().readValue(req.getInputStream(),UserCredential.class);


            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userCredential.getUsername(),userCredential.getPassword(), Collections.emptyList());

            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {

        Long now= System.currentTimeMillis();


       Object obj= auth.getCredentials();
        //Let's set the JWT Claims
        List<String> list = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            list.add(authority);
        }
        String token = Jwts.builder().claim("authorities", list)
                                            .setIssuedAt(new Date(now))
                                             .setExpiration(new Date(now+jwtConfig.getExpiration()*1000))
                                            .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .setSubject(auth.getName())
                                             .compact();

        System.out.print(token);

        response.addHeader(jwtConfig.getHeader(),jwtConfig.getPrefix()+token);
    }

    private static class UserCredential {

        String username,pass;

        public UserCredential(String username, String password) {
            this.username = username;
            this.pass  = password;
        }

        public UserCredential() {
        }

        public String getUsername() {
            return username;
        }

        public UserCredential setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return pass;
        }

        public UserCredential setPassword(String password) {
            this.pass = password;
            return this;
        }
    }
}
