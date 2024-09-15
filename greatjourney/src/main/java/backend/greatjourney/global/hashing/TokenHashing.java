package backend.greatjourney.global.hashing;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class TokenHashing {

//    public String getUserIdFromJWT(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody();
//        return claims.getSubject(); // sub에서 userId 추출
//    }
//
//
//        String jwtToken = resolveToken(request); // 헤더에서 토큰 추출
//        if (jwtToken != null && tokenProvider.validateToken(jwtToken)) {
//        Authentication authentication = tokenProvider.getAuthentication(jwtToken); // 사용자 정보 생성
//        SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext에 저장
//
        @Value("${jwt.secret.signin}")
        private  String secretKey;

        // JWT 토큰에서 사용자 ID 추출
        public String getUserIdFromJWT(String token) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // 'sub' 클레임에서 userId 추출
        }

        // 헤더에서 토큰을 읽어오는 메서드 (예: Authorization 헤더)
        public String resolveToken(String bearerToken) {
//            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            return null;
        }

        // 헤더에 있는 토큰을 읽어서 userId를 구하는 함수
        public String getUserIdFromRequest(String token) {
            String jwtToken = resolveToken(token); // 헤더에서 토큰 추출
            if (jwtToken != null && validateToken(jwtToken)) {
                return getUserIdFromJWT(jwtToken); // 토큰에서 사용자 ID 추출
            }
            return null;
        }

        // JWT 토큰의 유효성을 검사하는 메서드
        public boolean validateToken(String token) {
            try {
                Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
                return true;
            } catch (Exception e) {
                // 예외 처리 (유효하지 않은 토큰인 경우)
                return false;
            }
        }

//        // 사용자 인증을 설정하는 메서드 (필요할 경우 추가)
//        public void authenticateUser(HttpServletRequest request) {
//            String jwtToken = resolveToken(request); // 헤더에서 토큰 추출
//            if (jwtToken != null && validateToken(jwtToken)) {
//                Authentication authentication = getAuthentication(jwtToken); // 사용자 정보 생성
//                SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext에 저장
//            }
//        }
//
//        // 사용자 정보를 생성하는 메서드 (필요할 경우 구현)
//        private Authentication getAuthentication(String jwtToken) {
//            // 사용자의 정보를 기반으로 Authentication 객체를 생성하고 반환
//            // 예: UserDetailsService를 통해 사용자 정보를 가져와서 UsernamePasswordAuthenticationToken 반환
//            return null; // 실제 구현 필요
//        }
    }



