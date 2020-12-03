package generic.online.game.server.gogs.model.auth.jwt;

import generic.online.game.server.gogs.utils.GogsUserService;
import generic.online.game.server.gogs.model.auth.User;
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
    private final GogsUserService gogsUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userPrincipal(gogsUserService.getOneByUsername(username));
    }

    private UserPrincipal userPrincipal(User user) {
        return Optional.ofNullable(user).map( u ->
                new UserPrincipal(
                        u.getId(),
                        u.getUsername(),
                        u.getPassword(),
                        ROLE,
                        Collections.singletonList(new SimpleGrantedAuthority(ROLE))))
                .orElse(null);
    }
}
