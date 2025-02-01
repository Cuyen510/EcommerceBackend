package com.example.shopApp_backend.configurations;

import com.example.shopApp_backend.filters.JwtTokenFilter;
import com.example.shopApp_backend.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer:: disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(POST,apiPrefix+"/users/login").permitAll()
                            .requestMatchers(POST, apiPrefix+"/users/register").permitAll()
                            .requestMatchers(POST, apiPrefix+"/users/details").permitAll()
                            .requestMatchers(GET,apiPrefix+"/users").hasRole(Role.ADMIN)
                            .requestMatchers(PUT, apiPrefix+"/users").hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,apiPrefix+"/users").hasRole(Role.ADMIN)

                            .requestMatchers(GET, apiPrefix+"/roles").permitAll()
                            .requestMatchers(POST, apiPrefix+"/roles").hasRole(Role.ADMIN)
                            .requestMatchers(PUT,apiPrefix+"/roles").hasRole(Role.ADMIN)
                            .requestMatchers(DELETE,apiPrefix+"/roles").hasRole(Role.ADMIN)

                            .requestMatchers(GET, apiPrefix+"/products/**").permitAll()
                            .requestMatchers(GET, apiPrefix+"/products/images/**").permitAll()
                            .requestMatchers(POST, apiPrefix+"/products**").hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(PUT, apiPrefix+"/products").hasAnyRole(Role.ADMIN,Role.USER)
                            .requestMatchers(DELETE, apiPrefix+"/products").hasRole(Role.ADMIN)

                            .requestMatchers(GET, apiPrefix+"/categories").permitAll()
                            .requestMatchers(POST, apiPrefix+"/categories").hasRole(Role.ADMIN)
                            .requestMatchers(PUT, apiPrefix+"/categories").hasRole(Role.ADMIN)
                            .requestMatchers(DELETE, apiPrefix+"/categories").hasRole(Role.ADMIN)

                            .requestMatchers(GET, apiPrefix+"/order_details").hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(POST, apiPrefix+"/order_details").hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(PUT, apiPrefix+"/order_details").hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(DELETE, apiPrefix+"/order_details").hasAnyRole(Role.ADMIN, Role.USER)


                            .requestMatchers(POST, apiPrefix+"/orders").permitAll()
                            .requestMatchers(PUT, apiPrefix+"/orders").hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(DELETE, apiPrefix+"/orders").hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(GET, apiPrefix+"/orders").hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(GET, apiPrefix+"/orders/users**").hasAnyRole(Role.ADMIN, Role.USER)



                            .anyRequest().authenticated();
                });
        httpSecurity.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTION"));
                configuration.setAllowedHeaders(Arrays.asList("authorization","content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**",configuration);
                httpSecurityCorsConfigurer.configurationSource(source);


            }
        });



        return httpSecurity.build();

    }

}
