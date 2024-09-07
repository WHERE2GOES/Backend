//package backend.greatjourney.domain.login_notuse;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.GenericFilterBean;
//
//import java.io.IOException;
//import java.util.logging.Logger;
//
//@RequiredArgsConstructor
//public class JwtFilter extends GenericFilterBean {
//    private static final Logger logger = (Logger) LoggerFactory.getLogger(JwtFilter.class);
//    public static final String AUTHORIZATION_HEADER = "Authorization";
//    private final TokenProvider tokenProvider;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        String jwt = resolveToken(httpServletRequest);
//        String requestURI = httpServletRequest.getRequestURI();
//
//
//    }
//
//    private String resolveToken(HttpServletRequest request) {
//        String baererToken = request.getHeader(AUTHORIZATION_HEADER);
//
//        if(StringUtils.hasText(baererToken) || baererToken.startsWith("Bearer ")) {
//            return baererToken.substring(7);
//        }
//        return null;
//    }
//}
