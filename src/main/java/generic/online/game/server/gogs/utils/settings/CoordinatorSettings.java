package generic.online.game.server.gogs.utils.settings;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class CoordinatorSettings {
    private final boolean acceptBeforeStart;
    private final int maximumAcceptTime;
}
