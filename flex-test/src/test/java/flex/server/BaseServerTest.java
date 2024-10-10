package flex.server;

import flex.server.service.GreetingServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import plus.jdk.flex.server.global.FlexDelegateServer;

public class BaseServerTest {

    @BeforeAll
    public static void setUp() {
        FlexDelegateServer flexDelegateServer = new FlexDelegateServer();
        flexDelegateServer.registerService(new GreetingServiceImpl());
        flexDelegateServer.startFlexServer();
    }
}
