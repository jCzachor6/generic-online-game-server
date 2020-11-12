package czachor.jakub.ggs.impl;

import czachor.jakub.ggs.utils.AnonymousPrefixGenerator;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
@ToString
public class StringPrefixGenerator extends AnonymousPrefixGenerator {
    private final String prefix;

    public String generate() {
        return StringUtils.defaultString(prefix);
    }
}

