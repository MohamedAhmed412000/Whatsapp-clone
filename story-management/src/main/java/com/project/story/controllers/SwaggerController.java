package com.project.story.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Hidden
@Controller
@OpenAPIDefinition(
    servers = {
        @Server(url = "http://localhost:8080/core", description = "Core API via Gateway")
    }
)
@SecurityScheme(
    name = "keycloak",
    type = SecuritySchemeType.OAUTH2,
    bearerFormat = "JWT",
    scheme = "bearer",
    in = SecuritySchemeIn.HEADER,
    flows = @OAuthFlows(
        password = @OAuthFlow(
            authorizationUrl = "http://localhost:9090/realms/whatsapp-clone/protocol/openid-connect/auth",
            tokenUrl = "http://localhost:9090/realms/whatsapp-clone/protocol/openid-connect/token"
        )
    )
)
public class SwaggerController {

    @GetMapping("/")
    public RedirectView redirectToSwagger() {
        return new RedirectView("/swagger-ui/index.html");
    }

}
