package generic.online.game.server.gogs.utils.settings;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
public class GameUserSettings {
    private final boolean anonymousUser;
}
