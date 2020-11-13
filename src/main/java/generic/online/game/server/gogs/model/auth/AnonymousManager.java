package generic.online.game.server.gogs.model.auth;

import generic.online.game.server.gogs.model.auth.model.AuthRequest;
import generic.online.game.server.gogs.settings.GameUserSettings;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class AnonymousManager {
    private final AuthRequest authRequest;
    private final GameUserSettings settings;

    public AuthRequest setupAnonymous() {
        if (settings.isAnonymousUser() && (authRequest == null || StringUtils.isEmpty(authRequest.getUsername()))) {
            AuthRequest anonymousAccount = new AuthRequest();
            anonymousAccount.setUsername(settings.getAnonymousPrefixGenerator().generate());
            anonymousAccount.setPassword(settings.getAnonymousPasswordGenerator().generate());
            anonymousAccount.setAnonymous(true);
            return anonymousAccount;
        }
        return authRequest;
    }

    public AuthRequest setupAnonymousSuffix(String id) {
        AuthRequest request = new AuthRequest();
        request.setUsername(authRequest.getUsername() + id);
        request.setPassword(authRequest.getPassword());
        return request;
    }
}
