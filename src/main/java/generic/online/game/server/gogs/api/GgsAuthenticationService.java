package generic.online.game.server.gogs.api;

import generic.online.game.server.gogs.api.service.GgsUserService;
import generic.online.game.server.gogs.model.auth.AnonymousManager;
import generic.online.game.server.gogs.model.auth.jwt.JwtTokenProvider;
import generic.online.game.server.gogs.model.auth.model.AuthRequest;
import generic.online.game.server.gogs.model.auth.model.AuthResponse;
import generic.online.game.server.gogs.model.user.User;
import generic.online.game.server.gogs.model.user.UserBasicData;
import generic.online.game.server.gogs.settings.GameUserSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GgsAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final GameUserSettings gameUserSettings;
    private final GgsUserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticateUser(AuthRequest request) throws Exception {
        request = new AnonymousManager(request, gameUserSettings).setupAnonymous();
        if (request.isAnonymous()) {
            return registerUser(request);
        }
        return auth(request, request.getPassword());
    }

    public AuthResponse registerUser(AuthRequest request) throws Exception {
        User user = userService.createOne(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        if (gameUserSettings.isAnonymousUser() && request.isAnonymous()) {
            request = new AnonymousManager(request, gameUserSettings).setupAnonymousSuffix(user.getId());
            user.getBasicData().setUsername(request.getUsername());
            AuthResponse saved = edit(user, request.getPassword());
            return new AuthResponse(saved.getId(), saved.getUsername(), request.getPassword(), saved.getJwt());
        }
        return auth(request, request.getPassword());
    }

    private AuthResponse auth(AuthRequest request, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        String id = jwtTokenProvider.getClaims(jwt).get("id", String.class);
        return new AuthResponse(id, request.getUsername(), password, jwt);
    }

    private AuthResponse edit(User user, String password) {
        UserBasicData bd = user.getBasicData();
        this.userService.editUsername(user.getId(), bd.getUsername());
        AuthRequest request = new AuthRequest();
        request.setUsername(bd.getUsername());
        request.setPassword(bd.getPassword());
        return auth(request, password);
    }
}
