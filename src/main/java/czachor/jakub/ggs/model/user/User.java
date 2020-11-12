package czachor.jakub.ggs.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @Setter
    private String id;
    private final UserBasicData basicData = new UserBasicData();
    private final UserSearchCriteria searchCriteria = new UserSearchCriteria();
}
