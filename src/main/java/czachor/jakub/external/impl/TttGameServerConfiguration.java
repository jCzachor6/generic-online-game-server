package czachor.jakub.external.impl;

import czachor.jakub.ggs.impl.PinPasswordGenerator;
import czachor.jakub.ggs.impl.SimpleSearch;
import czachor.jakub.ggs.impl.StringPrefixGenerator;
import czachor.jakub.ggs.settings.CoordinatorSettings;
import czachor.jakub.ggs.settings.GameServerSettings;
import czachor.jakub.ggs.settings.GameUserSettings;
import czachor.jakub.ggs.settings.JwtSettings;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TttGameServerConfiguration {

    @Bean
    public GameUserSettings gameUserSettings() {
        return GameUserSettings.builder()
                .anonymousUser(true)
                .anonymousPrefixGenerator(new StringPrefixGenerator("player_"))
                .anonymousPasswordGenerator(new PinPasswordGenerator())
                .allowUsernameChange(true)
                .allowPasswordChange(true)
                .allowAccountDelete(true)
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
}
