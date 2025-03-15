package generic.online.game.server.gogs.api.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import generic.online.game.server.gogs.api.auth.AnonymousController;
import generic.online.game.server.gogs.api.auth.jwt.JwtTokenProvider;
import generic.online.game.server.gogs.api.auth.model.AuthResponse;
import generic.online.game.server.gogs.api.auth.model.User;
import generic.online.game.server.gogs.model.rooms.UuidGenerator;
import generic.online.game.server.gogs.utils.AnonymousUsernameGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Profile(AnonymousController.PROFILE)
@RequiredArgsConstructor
public class AnonymousService {
    private final AnonymousUsernameGenerator anonymousUsernameGenerator;
    private final JwtTokenProvider jwtTokenProvider;
    private final UuidGenerator idGenerator;

    public AuthResponse generateUser() throws JsonProcessingException {
        String id = idGenerator.generate();
        String username = anonymousUsernameGenerator.generate();
        List<String> roles = Collections.singletonList(AnonymousController.PROFILE);

        var token = jwtTokenProvider.generateToken(id, username, roles);

        return new AuthResponse(id, username, token.value());
    }
}
