package generic.online.game.server.gogs.api.service;

import com.corundumstudio.socketio.HandshakeData;
import generic.online.game.server.gogs.model.auth.AnonymousUserManager;
import generic.online.game.server.gogs.model.auth.User;
import generic.online.game.server.gogs.model.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.model.auth.jwt.JwtTokenProvider;
import generic.online.game.server.gogs.model.auth.model.AuthRequest;
import generic.online.game.server.gogs.model.auth.model.AuthResponse;
import generic.online.game.server.gogs.utils.GgsUserService;
import generic.online.game.server.gogs.utils.settings.GameUserSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final GgsUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AnonymousUserManager anonymousManager;

    public AuthResponse anonymousUser() throws Exception {
        return registerUser(anonymousManager.setupAnonymous());
    }

    public AuthResponse authenticateUser(AuthRequest request) {
        return auth(request, request.getPassword());
    }

    public boolean authenticateUser(HandshakeData request) {
        return jwtAuthenticationFilter.doFilterSocketToken(request);
    }

    public AuthResponse registerUser(AuthRequest request) throws Exception {
        validateUsername(request.getUsername());
        if (request.isAnonymous()) {
            User user = userService.createOne(request.getUsername(), passwordEncoder.encode(request.getPassword()));
            request = anonymousManager.setupAnonymousSuffix(request, user.getId());
            user.setUsername(request.getUsername());
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
        this.userService.editUsername(user.getId(), user.getUsername());
        AuthRequest request = new AuthRequest();
        request.setUsername(user.getUsername());
        request.setPassword(user.getPassword());
        return auth(request, password);
    }

    private void validateUsername(String username) throws Exception {
        User found = userService.getOneByUsername(username);
        if (found != null) {
            throw new Exception("Username already taken. ");
        }
    }
}
