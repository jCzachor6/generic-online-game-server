package generic.online.game.server.gogs.model.socket.message;

import generic.online.game.server.gogs.model.socket.Message;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageTest {
    private enum TestMessageType {
        ONE,
        TWO
    }

    private class TestMessage extends Message<TestMessageType> {
        private String testContent;
    }

    @Test
    public void shouldReturnTrueOnIsError() {
        TestMessage subject = new TestMessage();
        subject.setErrorMessage("ERROR");
        assertTrue(subject.isError());
    }

    @Test
    public void shouldGenerateJson(){
        TestMessage subject = new TestMessage();
        subject.setErrorMessage("ERROR");
        subject.setType(TestMessageType.ONE);
        assertTrue(subject.isError());
        assertEquals("{\"type\":\"ONE\",\"errorMessage\":\"ERROR\",\"error\":true}", subject.json());
    }
}
