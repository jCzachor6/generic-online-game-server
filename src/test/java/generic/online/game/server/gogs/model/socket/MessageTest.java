package generic.online.game.server.gogs.model.socket;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageTest {
    @EqualsAndHashCode(callSuper = true)
    @Data
    private class TestMessage extends Message {
        private String testContent;
    }

    @Test
    public void shouldReturnTrueOnIsError() {
        TestMessage subject = new TestMessage();
        subject.setErrorMessage("ERROR");
        assertTrue(subject.isError());
    }

    @Test
    public void shouldGenerateJson() {
        TestMessage subject = new TestMessage();
        subject.setTestContent("test");
        subject.setErrorMessage("ERROR");
        assertTrue(subject.isError());
        assertEquals("{\"errorMessage\":\"ERROR\",\"testContent\":\"test\"}", subject.json());
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    private class TestMessage2 extends Message {
        TestMessage2 testMessage2 = this;
    }

    @Test
    public void shouldGenerateEmptyJsonOnError() {
        TestMessage2 subject = new TestMessage2();
        subject.setErrorMessage("ERROR");
        assertTrue(subject.isError());
        assertEquals("{\"errorMessage\":\"Direct self-reference leading to cycle (through reference chain: generic.online.game.server.gogs.model.socket.MessageTest$TestMessage2[\\\"testMessage2\\\"])\"}",
                subject.json());
    }
}
