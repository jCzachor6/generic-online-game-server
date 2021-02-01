package generic.online.game.server.gogs;

import com.fasterxml.jackson.databind.ObjectMapper;
import generic.online.game.server.gogs.api.auth.AnonymousController;
import generic.online.game.server.gogs.api.auth.LoginController;
import generic.online.game.server.gogs.api.auth.RegisterController;
import generic.online.game.server.gogs.model.rooms.UuidGenerator;
import io.javalin.Javalin;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Configuration
public class GenericOnlineGameServer {
    public static final String GOGS_PACKAGE = "generic.online.game.server";

    public static void start(Consumer<GogsConfig> configConsumer) {
        GogsConfig config = new GogsConfig().setConfig(configConsumer);
        Objects.requireNonNull(config.basePackage, "Project package must not be null.");

        AnnotationConfigApplicationContext root = new AnnotationConfigApplicationContext();
        root.getEnvironment().setActiveProfiles(profiles(config));
        root.registerBean(GogsConfig.class, () -> config);
        root.registerBean(PropertiesLoader.class, PropertiesLoader::new);
        root.registerBean(UuidGenerator.class, () -> new UuidGenerator(10));
        root.scan(GOGS_PACKAGE, config.basePackage);
        root.refresh();


        root.getBean(Javalin.class).start(config.serverPort);
    }

    private static String[] profiles(GogsConfig config) {
        List<String> profiles = Arrays.stream(config.profiles).collect(Collectors.toCollection(ArrayList::new));
        if (config.authAnonymousUser) {
            profiles.add(AnonymousController.PROFILE);
        }
        if (config.authLogin) {
            profiles.add(LoginController.PROFILE);
        }
        if (config.authRegister) {
            profiles.add(RegisterController.PROFILE);
        }
        return profiles.toArray(new String[0]);
    }

    @Bean
    public Javalin javalin() {
        return Javalin.create((config) -> {
            config.showJavalinBanner = false;
        });
    }

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @Bean
    public PlaceholderConfigurerSupport propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
