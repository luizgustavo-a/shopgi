package tech.shopgi.authms.config.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .info(new Info()
                        .title("Authorization microservice for ShopGi")
                        .description("The AuthService microservice is responsible for user authentication and authorization," +
                                " generating and validating JWT tokens, and providing token refresh capabilities to " +
                                "secure access across the entire application.")
                        .contact(new Contact().email("luizalmeida.ads@gmail.com").url("https://github.com/luizgustavo-a"))
                        .license(new License().name("Apache 2.0").identifier("Apache-2.0"))
                        );

    }
}