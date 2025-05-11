package uz.pdp.exam8.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.csrf(csrf->{
            csrf.ignoringRequestMatchers("/logout");
        });
        http.authorizeHttpRequests(securityManager->{
            securityManager
                    .requestMatchers("/login")
                    .permitAll()
            .anyRequest().authenticated();
        });

        http.formLogin(manager->{
            manager.defaultSuccessUrl("/main");
        });

        http.logout((manager)->{
            manager.logoutUrl("/logout").logoutSuccessUrl("/");
        });

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
