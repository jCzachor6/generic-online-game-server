package generic.online.game.server.gogs.utils.annotations;

import generic.online.game.server.gogs.model.socket.Message;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface OnMessage {
    String value();

    Class<?> message() default Message.class;
}
