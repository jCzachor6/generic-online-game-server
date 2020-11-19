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

public class AnonymousUserManagerTest {
    public GameUserSettings settings = Mockito.mock(GameUserSettings.class);
    public AnonymousPrefixGenerator anonymousPrefixGenerator = Mockito.mock(AnonymousPrefixGenerator.class);
    public PasswordGenerator anonymousPasswordGenerator = Mockito.mock(PasswordGenerator.class);
    public AnonymousUserManager anonymousUserManager;

    @Before
    public void setup() {
        when(anonymousPrefixGenerator.generate()).thenReturn("prefix_");
        when(anonymousPasswordGenerator.generate()).thenReturn("1111");
        when(settings.isAnonymousUser()).thenReturn(true);
        anonymousUserManager = new AnonymousUserManager(settings, anonymousPrefixGenerator, anonymousPasswordGenerator);
    }

    @Test
    public void shouldGenerateAnonymousAuthRequest() {
        AuthRequest result = anonymousUserManager.setupAnonymous();
        assertEquals("prefix_", result.getUsername());
        assertEquals("1111", result.getPassword());
    }

    @Test
    public void shouldGenerateAnonymousAuthRequestOnEmptyUsername() {
        AuthRequest result = anonymousUserManager.setupAnonymous();
        assertEquals("prefix_", result.getUsername());
        assertEquals("1111", result.getPassword());
    }

    @Test
    public void shouldSetupSuffixToUsername() {
        AuthRequest request = new AuthRequest();
        request.setUsername("username");
        AuthRequest result = anonymousUserManager.setupAnonymousSuffix(request, "SUFFIX");
        assertEquals("usernameSUFFIX", result.getUsername());
    }

}
