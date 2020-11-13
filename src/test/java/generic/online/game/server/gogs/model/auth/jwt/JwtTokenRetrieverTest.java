package generic.online.game.server.gogs.model.auth.jwt;

import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JwtTokenRetrieverTest {

    @Test
    public void shouldRetrieveToken() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer token");
        Optional<String> result = new JwtTokenRetriever(req).getToken();
        assertEquals(Optional.of("token"), result);
    }

    @Test
    public void shouldReturnEmpty() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("123");
        Optional<String> result = new JwtTokenRetriever(req).getToken();
        assertEquals(Optional.empty(), result);
    }
}
