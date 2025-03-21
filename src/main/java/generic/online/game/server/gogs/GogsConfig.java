package generic.online.game.server.gogs;

import generic.online.game.server.gogs.api.auth.jwt.impl.JwtHmacSha256AlgorithmImpl;
import generic.online.game.server.gogs.api.auth.jwt.impl.JwtTokenAlgorithm;
import generic.online.game.server.gogs.api.auth.password.PasswordEncoder;
import generic.online.game.server.gogs.api.auth.password.RawPasswordEncoder;

import java.util.function.Consumer;

public class GogsConfig {
    public String basePackage;
    public String[] profiles = {};

    public int serverPort = 8080;
    public String wsServerNamespace = "/";
    public int wsServerPort = 9090;

    public boolean authRegister = true;
    public PasswordEncoder passwordEncoder = new RawPasswordEncoder();
    public boolean authAnonymousUser = true;
    public boolean authLogin = true;

    public String jwtSecret = "";
    public long jwtExpirationInMs = 604800000;
    public JwtTokenAlgorithm jwtEncryptionAlgorithm = new JwtHmacSha256AlgorithmImpl();

    public static Consumer<GogsConfig> noopConfig = (config) -> {
    };

    public GogsConfig setConfig(Consumer<GogsConfig> noopConfig) {
        noopConfig.accept(this);
        return this;
    }
}
