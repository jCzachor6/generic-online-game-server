package generic.online.game.server.gogs.api.auth.service;

import com.corundumstudio.socketio.HandshakeData;
import com.fasterxml.jackson.core.JsonProcessingException;
import generic.online.game.server.gogs.GogsConfig;
import generic.online.game.server.gogs.api.auth.jwt.JwtAuthenticationFilter;
import generic.online.game.server.gogs.api.auth.jwt.JwtTokenProvider;
import generic.online.game.server.gogs.api.auth.model.AuthRequest;
import generic.online.game.server.gogs.api.auth.model.AuthResponse;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.api.exception.GogsAuthenticationException;
import generic.online.game.server.gogs.api.exception.GogsValidationException;
import generic.online.game.server.gogs.utils.GogsUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final GogsUserService userService;
    private final GogsConfig gogsConfig;

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public AuthResponse authenticateUser(AuthRequest request) throws JsonProcessingException {
        String username = request.username();
        User user = userService.getOneByUsername(username);
        if (user == null) {
            throw new GogsAuthenticationException("User " + username + " not found. ");
        }
        if (!gogsConfig.passwordEncoder.matches(request.password(), user.password())) {
            throw new GogsAuthenticationException("Password does not match. ");
        }
        var token = jwtTokenProvider.generateToken(
                user.id(),
                user.username(),
                user.roles());

        return new AuthResponse(user.id(), user.username(), token.value());
    }

    public AuthResponse registerUser(AuthRequest request) throws GogsValidationException, JsonProcessingException {
        String username = request.username();
        String password = gogsConfig.passwordEncoder.encode(username);
        validateUsername(username);

        User user = userService.createOne(username, password);
        var token = jwtTokenProvider.generateToken(
                user.id(),
                user.username(),
                user.roles());
        return new AuthResponse(user.id(), user.username(), token.value());
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
