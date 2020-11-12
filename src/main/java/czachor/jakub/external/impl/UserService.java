package czachor.jakub.external.impl;

import czachor.jakub.ggs.api.service.GgsUserService;
import czachor.jakub.ggs.model.user.User;
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
    public User createOne(String username, String password) {
        TttUser user = new TttUser();
        user.setUsername(username);
        user.setPassword(password);
        return map(userRepository.save(user));
    }

    @Override
    public User edit(String id, String username, String password) {
        TttUser found = userRepository.getOne(Long.parseLong(id));
        found.setUsername(username);
        found.setPassword(password);
        return map(userRepository.save(found));
    }

    @Override
    public void removeUser(String id) {
        userRepository.delete(userRepository.getOne(Long.parseLong(id)));
    }
}
