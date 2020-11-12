package czachor.jakub.ggs.model.auth;

import czachor.jakub.ggs.model.auth.model.AuthRequest;
import czachor.jakub.ggs.settings.GameUserSettings;
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
