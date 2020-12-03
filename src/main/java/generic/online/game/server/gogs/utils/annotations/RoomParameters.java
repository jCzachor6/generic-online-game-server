package generic.online.game.server.gogs.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface RoomParameters {
    int CAPACITY_ANYONE = -1;
    int CAPACITY_PRESET_USERS = 0;

    int capacity() default CAPACITY_ANYONE;
}
