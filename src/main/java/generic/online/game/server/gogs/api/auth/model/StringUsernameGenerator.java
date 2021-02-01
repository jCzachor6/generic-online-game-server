package generic.online.game.server.gogs.api.auth.model;

import generic.online.game.server.gogs.utils.AnonymousUsernameGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class StringUsernameGenerator implements AnonymousUsernameGenerator {
    private long id;
    private final String prefix;

    @Override
    public String generate() {
        return StringUtils.defaultString(prefix) + ++id;
    }
}

