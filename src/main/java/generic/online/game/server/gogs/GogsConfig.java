package generic.online.game.server.gogs;

import io.jsonwebtoken.SignatureAlgorithm;
import io.liquer.pencil.encoder.SSHA224PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Consumer;

public class GogsConfig {
    public String basePackage;
    public String[] profiles = {};

    public int serverPort = 8080;
    public String wsServerNamespace = "/";
    public int wsServerPort = 9090;

    public boolean authRegister = true;
    public PasswordEncoder passwordEncoder = new SSHA224PasswordEncoder();
    public boolean authAnonymousUser = true;
    public boolean authLogin = true;

    public String jwtSecret = "";
    public long jwtExpirationInMs = 604800000;
    public SignatureAlgorithm jwtEncryptionAlgorithm = SignatureAlgorithm.HS256;

    public static Consumer<GogsConfig> noopConfig = (config) -> {
    };

    public GogsConfig setConfig(Consumer<GogsConfig> noopConfig) {
        noopConfig.accept(this);
        return this;
    }
}
