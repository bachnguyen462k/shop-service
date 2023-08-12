package com.example.demo.security;

import com.example.demo.model.jwt.JWTDeserializer;
import com.example.demo.model.pathLog.PathLog;
import com.example.demo.model.pathLog.PathLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@EnableConfigurationProperties(SecurityConfigurationProperties.class)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final SecurityConfigurationProperties properties;

    SecurityConfiguration(SecurityConfigurationProperties properties) {
        this.properties = properties;
    }

//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring().antMatchers(POST, "/users", "/users/login");
//    }


    @Autowired
    private PathLogService pathLogService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.formLogin().disable();
        http.logout().disable();
        http.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CustomFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers(GET, "/profiles/*").permitAll()
                .antMatchers(GET, "/articles/**").permitAll()
                .antMatchers(GET, "/tags/**").permitAll()
                .antMatchers(GET, "/actuator/**").permitAll()
                .antMatchers(GET, "/check-connection-DB").permitAll()
                .antMatchers(POST, "/users/login").permitAll()
                .anyRequest().authenticated();
    }


    public class CustomFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            // Truy cập thông tin request ở đây
            PathLog pathLog = new PathLog();
            pathLog.setIpAddress(request.getRemoteAddr());
            pathLog.setPath(request.getRequestURI());

            RequestMethod enumRequestMethod = RequestMethod.valueOf(request.getMethod());
            String requestMethodString = enumRequestMethod.name();
            pathLog.setCreatedAt(new Date());
            pathLog.setRequestMethod(requestMethodString);
            pathLog.setUserAgent(request.getHeader(USER_AGENT));
            pathLog.setSizeScreen(request.getHeader("Sizescreen"));
            pathLog.setMobile(request.getHeader("Sec-Ch-Ua-Mobile"));
            pathLog.setPlatform(request.getHeader("Sec-Ch-Ua-Platform"));
            pathLog.setRequestId(request.getHeader("Requestid"));
            pathLog.setRole(request.getHeader("Role"));
            pathLog.setReferer(request.getHeader(REFERER));
            pathLog.setToken(request.getHeader(AUTHORIZATION));
            pathLog.setStatus(response.getStatus());
            pathLogService.createNewPathLog(pathLog);
            filterChain.doFilter(request, response);
        }
    }

    @Bean
    JWTAuthenticationProvider jwtAuthenticationProvider(JWTDeserializer jwtDeserializer) {
        return new JWTAuthenticationProvider(jwtDeserializer);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "HEAD", "POST", "DELETE", "PUT")
                .allowedOrigins(properties.getAllowedOrigins().toArray(new String[0]))
                .allowedHeaders("*")
                .allowCredentials(false);
    }

}

@ConstructorBinding
@ConfigurationProperties("security")
class SecurityConfigurationProperties {
    private final List<String> allowedOrigins;

    SecurityConfigurationProperties(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }
}