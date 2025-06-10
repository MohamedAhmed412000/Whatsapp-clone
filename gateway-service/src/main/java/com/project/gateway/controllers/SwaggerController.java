package com.project.gateway.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.RedirectView;

@Hidden
@Controller
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
