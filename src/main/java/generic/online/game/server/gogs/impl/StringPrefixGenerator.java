package generic.online.game.server.gogs.impl;

import generic.online.game.server.gogs.utils.AnonymousPrefixGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class StringPrefixGenerator implements AnonymousPrefixGenerator {
    private final String prefix;

    @Override
    public String generate() {
        return StringUtils.defaultString(prefix);
    }
}

