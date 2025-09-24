package br.com.apiunigym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder( ) {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        http
            .csrf(csrf -> csrf.disable( ))
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Adicione esta linha para habilitar CORS
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(antMatcher("/alunos/**")).permitAll()
                .requestMatchers(antMatcher("/login/**")).permitAll()
                .anyRequest().authenticated()
            );
        return http.build( );
    }

    // Adicione este novo Bean para configurar o CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://127.0.0.1:5500" ); // Permita a origem do seu frontend
        configuration.addAllowedMethod("*"); // Permita todos os métodos (GET, POST, PUT, DELETE, etc.)
        configuration.addAllowedHeader("*"); // Permita todos os cabeçalhos
        configuration.setAllowCredentials(true); // Permita o envio de credenciais (cookies, tokens de autorização)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplique esta configuração a todos os endpoints
        return source;
    }
}
