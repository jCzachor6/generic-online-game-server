package generic.online.game.server.external.impl;

import generic.online.game.server.gogs.api.service.GgsUserService;
import generic.online.game.server.gogs.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("userService")
public class UserService implements GgsUserService<TttUser> {
    private final UserRepository userRepository;

    @Override
    public User map(TttUser tttUser) {
        User user = new User();
        user.setId(tttUser.getId().toString());
        user.getBasicData().setUsername(tttUser.getUsername());
        user.getBasicData().setPassword(tttUser.getPassword());
        return user;
    }

    @Override
    public User getOne(String id) {
        return map(userRepository.getOne(Long.parseLong(id)));
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

    @Override
    public void removeUser(String id) {
        userRepository.delete(userRepository.getOne(Long.parseLong(id)));
    }
}
