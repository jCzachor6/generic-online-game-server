package czachor.jakub.ggs.settings;

import czachor.jakub.ggs.utils.AnonymousPrefixGenerator;
import czachor.jakub.ggs.utils.PasswordGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class GameUserSettings {
    private final boolean allowUsernameChange;
    private final boolean allowPasswordChange;
    private final boolean allowAccountDelete;

    private final boolean anonymousUser;
    private final AnonymousPrefixGenerator anonymousPrefixGenerator;
    private final PasswordGenerator anonymousPasswordGenerator;
}
