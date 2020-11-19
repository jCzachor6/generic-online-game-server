package generic.online.game.server.external.impl.user;

import generic.online.game.server.gogs.api.service.GgsUserService;
import generic.online.game.server.gogs.model.auth.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
@Service("userService")
public class UserService implements GgsUserService<TttUser> {
    private final UserRepository userRepository;

    @Override
    public User map(TttUser tttUser) {
        return Optional.ofNullable(tttUser).map(u -> {
            User user = new User();
            user.setId(tttUser.getId().toString());
            user.setUsername(tttUser.getUsername());
            user.setPassword(tttUser.getPassword());
            user.setCriteria(new HashMap<>());
            return user;
        }).orElse(null);
    }

    @Override
    public User getOneByUsername(String username) {
        return map(userRepository.getFirstByUsername(username));
    }

    @Override
    public User createOne(String username, String password) {
        TttUser user = new TttUser();
        user.setUsername(username);
        user.setPassword(password);
        return map(userRepository.save(user));
    }

    @Override
    public User editUsername(String id, String username) {
        TttUser found = userRepository.getOne(Long.parseLong(id));
        found.setUsername(username);
        return map(userRepository.save(found));
    }
}
