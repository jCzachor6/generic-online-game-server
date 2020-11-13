package generic.online.game.server.gogs.api;

import generic.online.game.server.gogs.model.auth.model.AuthRequest;
import generic.online.game.server.gogs.model.auth.model.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/ggs/api/auth")
public class GgsAuthenticationController {
    private final GgsAuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) throws Exception {
        return ResponseEntity.ok(service.authenticateUser(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AuthRequest request) throws Exception {
        return ResponseEntity.ok(service.registerUser(request));
    }
}
