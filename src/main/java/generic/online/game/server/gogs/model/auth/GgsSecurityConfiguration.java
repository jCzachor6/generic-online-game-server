package generic.online.game.server.gogs.model.auth;

import generic.online.game.server.gogs.model.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.model.auth.jwt.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class GgsSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/auth/**").permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {

        authenticationManagerBuilder
                .userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
