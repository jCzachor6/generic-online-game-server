package generic.online.game.server.gogs.settings;

import generic.online.game.server.gogs.utils.AnonymousPrefixGenerator;
import generic.online.game.server.gogs.utils.PasswordGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class GameUserSettings {
    private final boolean anonymousUser;
    private final AnonymousPrefixGenerator anonymousPrefixGenerator;
    private final PasswordGenerator anonymousPasswordGenerator;
}
