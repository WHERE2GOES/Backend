//package backend.greatjourney.domain.login_notuse;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class JwtConfig {
//
//    private final TokenProvider tokenProvider;
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/**")
//                        .permitAll()
////                        .requestMatchers("/api/v1/admin").hasAnyAuthority(Role.ROLE_ADMIN.name())
////                        .requestMatchers("/api/v1/user").hasAnyAuthority(Role.ROLE_USER.name())
////                        .requestMatchers("/api/vi/no_user").hasAnyAuthority(Role.ROEL_NO_USER.name())
//                        .anyRequest().authenticated())
//
//                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return httpSecurity.build();
////        httpSecurity
////                // CSRF 보호 비활성화
////                .csrf(AbstractHttpConfigurer::disable)
////
////                // 예외 처리 설정
////                .exceptionHandling(exceptionHandling ->
////                        exceptionHandling
////                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
////                                .accessDeniedHandler(jwtAccessDeniedHandler)
////                )
////
////                // Content Security Policy 설정을 통해 클릭재킹 방지
////                .headers(headers ->
////                        headers
////                                .contentSecurityPolicy("frame-ancestors 'self'")
////                )
////
////                // 세션을 사용하지 않기 위한 설정
////                .sessionManagement(sessionManagement ->
////                        sessionManagement
////                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                )
////
////                // 요청에 대한 접근 권한 설정
////                .authorizeHttpRequests(requests ->
////                        requests
////                                .requestMatchers("/api/authenticate").permitAll()
////                                .requestMatchers("/api/signup").permitAll()
////                                .requestMatchers(PathRequest.toH2Console()).permitAll()
////                                .requestMatchers("/favicon.ico").permitAll()
////                                .anyRequest().authenticated()
////                )
////
////                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
////                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider),
////                        UsernamePasswordAuthenticationFilter.class);
////
////        return httpSecurity.build();
//    }
//
////    @Bean
////    public AuthenticationProvider authenticationProvider() {
////        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
////        authenticationProvider.setUserDetailsService(userService.userDetailsService());
////        authenticationProvider.setPasswordEncoder(passwordEncoder());
////        return authenticationProvider;
////    }
//}
