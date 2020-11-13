package generic.online.game.server.gogs.settings;

import generic.online.game.server.gogs.utils.SearchCriteria;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class CoordinatorSettings {
    private final int maximumSearchTime;
    private final SearchCriteria searchCriteria;
    private final boolean acceptBeforeStart;
    private final int maximumAcceptTime;
}
