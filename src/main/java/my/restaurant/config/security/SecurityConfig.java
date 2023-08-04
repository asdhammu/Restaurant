package my.restaurant.config.security;

import my.restaurant.dto.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/admin/**").hasAuthority(UserRole.ADMIN.toString())
                        .requestMatchers("/user/**").hasAnyAuthority(UserRole.USER.toString(), UserRole.ADMIN.toString())
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login").defaultSuccessUrl("/")
                        .permitAll()
                        .successHandler(new RefererRedirectionAuthenticationSuccessHandler())
                )
                .logout(logout -> logout.permitAll().logoutSuccessUrl("/"));

        return http.build();
    }

}
