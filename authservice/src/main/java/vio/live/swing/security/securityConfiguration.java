package vio.live.swing.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import vio.live.swing.config.JwtConfig;
import vio.live.swing.controller.UserController;
import vio.live.swing.filter.JwtAuthenticationfilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class securityConfiguration extends WebSecurityConfigurerAdapter{


    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    UserController userController;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        System.out.print("hi executed");

        http.
                csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint((req,res,e)->{res.sendError(HttpServletResponse.SC_UNAUTHORIZED);

        }).and().addFilter(new JwtAuthenticationfilter(authenticationManager(),jwtConfig))
        .authorizeRequests().
        antMatchers(HttpMethod.POST,jwtConfig.getPrefix()).permitAll()

        .anyRequest().authenticated();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userController).passwordEncoder(passwordEncoder());
    }

    @Bean
    public JwtConfig getJwtConfig() {

        return  new JwtConfig();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
