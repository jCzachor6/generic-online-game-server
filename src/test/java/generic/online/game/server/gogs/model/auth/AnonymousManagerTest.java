package generic.online.game.server.gogs.model.auth;

import generic.online.game.server.gogs.model.auth.model.AuthRequest;
import generic.online.game.server.gogs.settings.GameUserSettings;
import generic.online.game.server.gogs.utils.AnonymousPrefixGenerator;
import generic.online.game.server.gogs.utils.PasswordGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class AnonymousManagerTest {
    public GameUserSettings settings;
    public AnonymousPrefixGenerator anonymousPrefixGenerator = Mockito.mock(AnonymousPrefixGenerator.class);
    public PasswordGenerator anonymousPasswordGenerator = Mockito.mock(PasswordGenerator.class);

    @Before
    public void setup() {
        when(anonymousPrefixGenerator.generate()).thenReturn("prefix_");
        when(anonymousPasswordGenerator.generate()).thenReturn("1111");
        settings = GameUserSettings
                .builder()
                .anonymousPasswordGenerator(anonymousPasswordGenerator)
                .anonymousPrefixGenerator(anonymousPrefixGenerator)
                .anonymousUser(true)
                .build();

    }

    @Test
    public void shouldGenerateAnonymousAuthRequest() {
        AuthRequest result = new AnonymousManager(null, settings).setupAnonymous();
        assertEquals("prefix_", result.getUsername());
        assertEquals("1111", result.getPassword());
    }

    @Test
    public void shouldGenerateAnonymousAuthRequestOnEmptyUsername() {
        AuthRequest result = new AnonymousManager(new AuthRequest(), settings).setupAnonymous();
        assertEquals("prefix_", result.getUsername());
        assertEquals("1111", result.getPassword());
    }

    @Test
    public void shouldReturnSameAuthRequest() {
        AuthRequest request = new AuthRequest();
        request.setUsername("username");
        request.setPassword("password");
        AuthRequest result = new AnonymousManager(request, settings).setupAnonymous();
        assertEquals("username", result.getUsername());
        assertEquals("password", result.getPassword());
    }

    @Test
    public void shouldSetupSuffixToUsername() {
        AuthRequest request = new AuthRequest();
        request.setUsername("username");
        AuthRequest result = new AnonymousManager(request, settings).setupAnonymousSuffix("SUFFIX");
        assertEquals("usernameSUFFIX", result.getUsername());
    }

}