package backend.greatjourney.config;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {
//    @Bean
//    public OpenAPI api() {
//        SecurityScheme apiKey = new SecurityScheme()
//                .type(SecurityScheme.Type.APIKEY)
//                .in(SecurityScheme.In.HEADER)
//                .name("Authorization");
//
//        SecurityRequirement securityRequirement = new SecurityRequirement()
//                .addList("Bearer Token");
//
//        return new OpenAPI()
//                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
//                .addSecurityItem(securityRequirement);
//    }
}
