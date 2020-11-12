package czachor.jakub.ggs.api;

import czachor.jakub.ggs.api.service.GgsUserService;
import czachor.jakub.ggs.model.auth.AnonymousManager;
import czachor.jakub.ggs.model.auth.model.AuthRequest;
import czachor.jakub.ggs.model.auth.model.AuthResponse;
import czachor.jakub.ggs.model.user.User;
import czachor.jakub.ggs.model.user.UserBasicData;
import czachor.jakub.ggs.settings.GameUserSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/ggs/api/auth")
public class GgsAuthenticationController {
    private final GameUserSettings gameUserSettings;
    private final GgsUserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) throws Exception {
        request = new AnonymousManager(request, gameUserSettings).setupAnonymous();
        if (request.isAnonymous()) {
            return registerUser(request);
        }
        return ResponseEntity.ok(auth(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AuthRequest request) throws Exception {
        User user = userService.createOne(request.getUsername(), request.getPassword());
        if (gameUserSettings.isAnonymousUser() && request.isAnonymous()) {
            request = new AnonymousManager(request, gameUserSettings).setupAnonymousSuffix(user.getId());
            user.getBasicData().setUsername(request.getUsername());
            AuthResponse saved = edit(user);
            return ResponseEntity.ok(
                    new AuthResponse(saved.getId(), saved.getUsername(), request.getPassword(), saved.getJwt())
            );
        }
        return ResponseEntity.ok(auth(request));
    }

    @PutMapping("/")
    public ResponseEntity<AuthResponse> editUser(@RequestBody AuthRequest edited) throws Exception {
        String id = ""; //TODO
        User user = userService.getOne(id);
        if (gameUserSettings.isAllowUsernameChange()) {
            user.getBasicData().setUsername(edited.getUsername());
        }
        if (gameUserSettings.isAllowPasswordChange()) {
            user.getBasicData().setPassword(edited.getPassword());
        }
        return ResponseEntity.ok(edit(user));
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteUser() {
        String id = ""; //TODO
        userService.removeUser(id);
        return ResponseEntity.noContent().build();
    }

    private AuthResponse auth(AuthRequest request) throws Exception {
        return new AuthResponse("", request.getUsername(), "", ""); //TODO
    }

    private AuthResponse edit(User user) throws Exception {
        UserBasicData bd = user.getBasicData();
        this.userService.edit(user.getId(), bd.getUsername(), bd.getPassword());
        AuthRequest request = new AuthRequest();
        request.setUsername(bd.getUsername());
        request.setPassword(bd.getPassword());
        return auth(request);
    }
}
