package generic.online.game.server.gogs.model.auth;

import generic.online.game.server.gogs.model.auth.model.AuthRequest;
import generic.online.game.server.gogs.settings.GameUserSettings;
import generic.online.game.server.gogs.utils.AnonymousPrefixGenerator;
import generic.online.game.server.gogs.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AnonymousUserManager {
    private final GameUserSettings settings;
    private final AnonymousPrefixGenerator anonymousPrefixGenerator;
    private final PasswordGenerator anonymousPasswordGenerator;

    public AuthRequest setupAnonymous() {
        AuthRequest authRequest = new AuthRequest();
        if (settings.isAnonymousUser()) {
            AuthRequest anonymousAccount = new AuthRequest();
            anonymousAccount.setUsername(anonymousPrefixGenerator.generate());
            anonymousAccount.setPassword(anonymousPasswordGenerator.generate());
            anonymousAccount.setAnonymous(true);
            return anonymousAccount;
        }
        return authRequest;
    }

    public AuthRequest setupAnonymousSuffix(AuthRequest authRequest, String id) {
        AuthRequest request = new AuthRequest();
        if (settings.isAnonymousUser()) {
            request.setUsername(authRequest.getUsername() + id);
            request.setPassword(authRequest.getPassword());
        }
        return request;
    }
}
