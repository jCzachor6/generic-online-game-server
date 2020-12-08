package generic.online.game.server.gogs.model.socket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class Message {
    private String errorMessage;

    @JsonIgnore
    public boolean isError() {
        return StringUtils.isNotEmpty(errorMessage);
    }

    public String json() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            Message m = new Message();
            m.errorMessage = e.getMessage();
            return m.json();
        }
    }
}
