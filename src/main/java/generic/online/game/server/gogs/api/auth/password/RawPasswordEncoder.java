package generic.online.game.server.gogs.api.auth.password;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RawPasswordEncoder implements PasswordEncoder {

    public RawPasswordEncoder() {
        log.warn("Warning: RawPasswordEncoder is being used. This encoder does not hash passwords and should not be used in production!");
    }

    @Override
    public String encode(String rawPassword) {
        return rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}

