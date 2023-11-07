package com.exe.persai.config;

import com.exe.persai.constants.SwaggerApiTag;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "token_auth",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@OpenAPIDefinition(
        info = @Info(
                title = "Swagger API Doc for PerSAI Web Service",
                description = "This is list of endpoints and documentations of REST API for PerSAI Web Service",
                version = "1.0"
        ),
        servers = {
                @Server(url = "${API-DOC-SERVER-DOMAIN-1}",
                        description = "Development server domain for profile " + "${spring.profiles.active}"),
                @Server(url = "${API-DOC-SERVER-DOMAIN-2}",
                        description = "Development server domain for profile " + "${spring.profiles.active}")
        },
        security = {
                @SecurityRequirement(name = "token_auth")
        },
        tags = {
                @Tag(name = SwaggerApiTag.WELCOME, description = SwaggerApiTag.WELCOME_TAG_PURPOSE),
                @Tag(name = SwaggerApiTag.AUTHENTICATE, description =  SwaggerApiTag.AUTHENTICATE_TAG_PURPOSE),
                @Tag(name = SwaggerApiTag.LOGIN, description =  SwaggerApiTag.LOGIN_TAG_PURPOSE),
                @Tag(name = SwaggerApiTag.USER, description =  SwaggerApiTag.USER_TAG_PURPOSE),
                @Tag(name = SwaggerApiTag.SUBSCRIPTION, description =  SwaggerApiTag.SUBSCRIPTION_TAG_PURPOSE),
                @Tag(name = SwaggerApiTag.REFERRAL, description =  SwaggerApiTag.REFERRAL_TAG_PURPOSE),
                @Tag(name = SwaggerApiTag.STUDY_SET, description =  SwaggerApiTag.STUDY_SET_TAG_PURPOSE),
                @Tag(name = SwaggerApiTag.CHAT_GPT, description =  SwaggerApiTag.CHAT_GPT_TAG_PURPOSE),
                @Tag(name = SwaggerApiTag.IMAGE, description =  SwaggerApiTag.IMAGE_TAG_PURPOSE)
        }
)
public class ApiDocConfig {
}
