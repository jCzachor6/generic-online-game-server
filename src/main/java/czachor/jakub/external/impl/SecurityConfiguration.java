package czachor.jakub.external.impl;

import czachor.jakub.ggs.model.auth.GgsSecurityConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration extends GgsSecurityConfiguration {

    public SecurityConfiguration(JwtUserDetailsService jwtUserDetailsService) {
        super(jwtUserDetailsService);
    }
}
