package czachor.jakub.ggs.settings;

import czachor.jakub.ggs.utils.SearchCriteria;
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
