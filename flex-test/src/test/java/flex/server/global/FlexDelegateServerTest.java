package flex.server.global;

import org.junit.Test;
import plus.jdk.flex.server.global.FlexDelegateServer;

public class FlexDelegateServerTest {

    @Test
    public void testStartServer() {
        FlexDelegateServer delegateServer = new FlexDelegateServer();
        delegateServer.startFlexServer();
    }
}
