package generic.online.game.server.external.impl;

import generic.online.game.server.gogs.impl.PinPasswordGenerator;
import generic.online.game.server.gogs.impl.SimpleSearch;
import generic.online.game.server.gogs.impl.StringPrefixGenerator;
import generic.online.game.server.gogs.settings.CoordinatorSettings;
import generic.online.game.server.gogs.settings.GameServerSettings;
import generic.online.game.server.gogs.settings.GameUserSettings;
import generic.online.game.server.gogs.settings.JwtSettings;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = "generic.online.game.server.gogs")
public class TttGameServerConfiguration {

    @Bean
    public GameUserSettings gameUserSettings() {
        return GameUserSettings.builder()
                .anonymousUser(true)
                .anonymousPrefixGenerator(new StringPrefixGenerator("player_"))
                .anonymousPasswordGenerator(new PinPasswordGenerator())
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
    public GameServerSettings gameServerSettings() {
        return GameServerSettings.builder()
                .tickRate(0)
                .closeEmptyRooms(true)
                .allowRejoin(true)
                .roomMaxCapacity(10)
                .maximumRejoinTime(300)
                .maximumJoinTime(30)
                .saveReplays(false)
                .privateRooms(true)
                .build();
    }

    @Bean
    public CoordinatorSettings coordinatorSettings() {
        return CoordinatorSettings.builder()
                .maximumSearchTime(300)
                .acceptBeforeStart(true)
                .maximumAcceptTime(30)
                .searchCriteria(new SimpleSearch(2))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
