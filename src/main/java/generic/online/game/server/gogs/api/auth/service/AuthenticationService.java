package generic.online.game.server.gogs.api.auth.service;

import com.corundumstudio.socketio.HandshakeData;
import generic.online.game.server.gogs.GogsConfig;
import generic.online.game.server.gogs.api.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.api.auth.jwt.JwtTokenProvider;
import generic.online.game.server.gogs.api.auth.model.AuthRequest;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.api.exception.GogsAuthenticationException;
import generic.online.game.server.gogs.api.exception.GogsValidationException;
import generic.online.game.server.gogs.utils.GogsUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final GogsUserService userService;
    private final GogsConfig gogsConfig;

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public User authenticateUser(AuthRequest request) {
        String username = request.getUsername();
        User user = userService.getOneByUsername(username);
        if (user == null) {
            throw new GogsAuthenticationException("User " + username + " not found. ");
        }
        if (!gogsConfig.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new GogsAuthenticationException("Password does not match. ");
        }
        String token = jwtTokenProvider.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRoles());
        return user.withToken(token);
    }

    public User registerUser(AuthRequest request) throws GogsValidationException {
        String username = request.getUsername();
        String password = gogsConfig.passwordEncoder.encode(username);
        validateUsername(username);

        User user = userService.createOne(username, password);
        String token = jwtTokenProvider.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRoles());
        return user.withToken(token);
    }

    private void validateUsername(String username) throws GogsValidationException {
        User found = userService.getOneByUsername(username);
        if (found != null) {
            throw new GogsValidationException("Username already taken. ");
        }
    }

    public boolean authenticateUser(HandshakeData request) {
        return jwtAuthenticationFilter.doFilterSocketToken(request);
    }
}
