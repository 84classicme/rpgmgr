package io.festerson.rpgvault;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "RPGmgr API", version = "1.0", description = "Helping DMs manage their campaigns since 1904."))
@SecurityScheme(name = "jwt", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                //bearer auth with oAuth2
//                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
//                .components(new Components().addSecuritySchemes("oAuth2",                   new SecurityScheme()                                                                .type(SecurityScheme.Type.OAUTH2)                                                               .flows(getOAuthFlows())
//)
//
///// Bearer AUTH security config settings. ## for id-token.
//.addSecuritySchemes("bearerAuth",                                                       new SecurityScheme()
//.type(SecurityScheme.Type.HTTP)
//.scheme("bearer")
//.bearerFormat("JWT")
//)
//).info(new Info().title("RPGMGR API"));
//    }
//
//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//            .group("public-api")
//            .pathsToMatch("/**")
//            .build();
//    }

}
