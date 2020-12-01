package generic.online.game.server.external.impl;

import generic.online.game.server.gogs.impl.PinPasswordGenerator;
import generic.online.game.server.gogs.impl.RoomUuidGenerator;
import generic.online.game.server.gogs.impl.SimpleSearch;
import generic.online.game.server.gogs.impl.StringPrefixGenerator;
import generic.online.game.server.gogs.utils.*;
import generic.online.game.server.gogs.utils.settings.*;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "generic.online.game.server.gogs")
public class TttGameServerConfiguration {

    @Bean
    public GameUserSettings gameUserSettings() {
        return GameUserSettings.builder()
                .anonymousUser(true)
                .build();
    }

    @Bean
    public JwtSettings jwtSettings() {
        return JwtSettings.builder()
                .encryptionAlgorithm(SignatureAlgorithm.HS256)
                .expirationInMs(604800000)
                .secret("secret")
                .build();
    }

    @Bean
    public SocketSettings socketSettings() {
        return SocketSettings.builder()
                .namespace("/ttt")
                .port(9092)
                .build();
    }

    @Bean
    public AnonymousPrefixGenerator anonymousPrefixGenerator() {
        return new StringPrefixGenerator("player_");
    }

    @Bean
    public PasswordGenerator passwordGenerator() {
        return new PinPasswordGenerator();
    }
}
