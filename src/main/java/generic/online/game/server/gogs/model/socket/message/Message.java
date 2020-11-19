package generic.online.game.server.gogs.model.socket.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public abstract class Message<TYPE> {
    private String destination;
    private TYPE type;
    private String errorMessage;

    public boolean isError() {
        return StringUtils.isNotEmpty(errorMessage);
    }

    public String json() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
