package generic.online.game.server.gogs.model.auth.jwt;

import generic.online.game.server.gogs.api.service.GgsUserService;
import generic.online.game.server.gogs.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {
    public static String ROLE = "USER";
    private final GgsUserService ggsUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userPrincipal(ggsUserService.getOneByUsername(username));
    }

    private UserPrincipal userPrincipal(User user) {
        return Optional.ofNullable(user).map( u ->
                new UserPrincipal(
                        u.getId(),
                        u.getBasicData().getUsername(),
                        u.getBasicData().getPassword(),
                        ROLE,
                        Collections.singletonList(new SimpleGrantedAuthority(ROLE))))
                .orElse(null);
    }
}
