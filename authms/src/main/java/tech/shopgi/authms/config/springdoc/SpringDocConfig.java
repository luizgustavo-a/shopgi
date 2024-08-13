package tech.shopgi.authms.config.springdoc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Authorization microservice for ShopGi")
                .description("The AuthService microservice is responsible for user authentication and authorization, " +
                        "generating and validating JWT tokens, and providing token refresh capabilities to " +
                        "secure access across the entire application.")
                .contact(new springfox.documentation.service.Contact("Luiz Almeida", "https://github.com/luizgustavo-a", "luizalmeida.ads@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .version("1.0.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("bearerAuth", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(
                        new springfox.documentation.service.SecurityReference("bearerAuth", new springfox.documentation.service.AuthorizationScope[0])
                ))
                .build();
    }
}
