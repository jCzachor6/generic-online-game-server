package generic.online.game.server.gogs.model.auth.jwt;

import com.corundumstudio.socketio.HandshakeData;
import generic.online.game.server.gogs.model.auth.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static generic.online.game.server.gogs.model.auth.jwt.JwtTokenRetriever.AUTHORIZATION_HEADER;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            new JwtTokenRetriever(request.getHeader(AUTHORIZATION_HEADER)).getToken().ifPresent(jwt -> {
                if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                    String username = jwtTokenProvider.getUsernameFromJWT(jwt);
                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            });
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

    public boolean doFilterSocketToken(HandshakeData handshakeData) {
        String token = handshakeData.getSingleUrlParam("token");
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsernameFromJWT(token);
            logger.info("[Authorization  user = " + username + "]");
            return jwtUserDetailsService.loadUserByUsername(username) != null;
        }
        return false;
    }

    public User getUserFromToken(String token) {
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            Claims claims = jwtTokenProvider.getClaims(token);
            User user = new User();
            user.setUsername(claims.get("username", String.class));
            user.setId(claims.get("id", String.class));
            user.setToken(token);
            return user;
        }
        return null;
    }
}
