package uz.dachatop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uz.dachatop.util.MD5Util;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final UserDetailsService userDetailsService;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final JwtAuthenticationFilter jwtTokenFilter;
    public static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/api/v1/auth/**",
            "api/v1/init/**",
            "/api/v1/attach/**",
            "/api/v1/convenience/public/**",

            "/api/v1/region/public/",
            "/api/v1/region/public",
            "/api/v1/profile/public/",
            "/api/v1/profile/public",
            "/api/v1/country/public/**",
            "/api/v1/country/public",
            "/api/v1/message/public/**",
            "/api/v1/message",
            "/api/v1/district/public/regionId/*",
            "/api/v1/district/public/regionId/**",
            "/api/v1/territory/public/**",
            "/api/v1/territory/public",
            "/api/v1/apartment-type/**",
            "/api/v1/apartment/public/**",
            "/api/v1/dacha/public/**",
            "/api/v1/hotel/public/**",
            "/api/v1/extreme/public/**",
            "/api/v1/travel/public/**",
            "/api/v1/camp/public/**",
            "/api/v1/room-type/public/**",
            "/api/v1/extreme-type/public/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization
        http.cors().and().csrf().disable();
        http.authorizeHttpRequests()
//                .requestMatchers(HttpMethod.GET, "/api/v1/district/regionId/*").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated().and().addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
//           http.exceptionHandling().authenticationEntryPoint(authEntryPointJwt);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                String md5 = MD5Util.getMd5(rawPassword.toString());
                return md5.equals(encodedPassword);
            }
        };
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedMethods(CorsConfiguration.ALL)
                        .allowedHeaders(CorsConfiguration.ALL)
                        .allowedOriginPatterns(CorsConfiguration.ALL)
                        .allowCredentials(true);
            }
        };
    }
}
