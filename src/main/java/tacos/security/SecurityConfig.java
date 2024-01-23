package tacos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@SuppressWarnings("unused")
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, MvcRequestMatcher.Builder mvc) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(antMatcher("/design"), antMatcher("/orders")).hasRole("USER")
                        .requestMatchers(mvc.pattern("/"), mvc.pattern("/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .formLogin(login -> login.loginPage("/login").defaultSuccessUrl("/design"));

        return httpSecurity.build();
    }

    //TODO implement dev/production profiles

//      h2db/dev config
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, MvcRequestMatcher.Builder mvc) throws Exception {
//        httpSecurity
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers(toH2Console())
//                        .disable()
//                )
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(toH2Console()).permitAll()
//                        .requestMatchers(antMatcher("/design"), antMatcher("/orders")).hasRole("USER")
//                        .requestMatchers(mvc.pattern("/"), mvc.pattern("/**")).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
//                .formLogin(login -> login.loginPage("/login").defaultSuccessUrl("/design"));
//
//        return httpSecurity.build();
//    }
}
