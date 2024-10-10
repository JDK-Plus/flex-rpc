package flex.client.global;

import flex.common.provider.GreetingService;
import flex.server.BaseServerTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.jdk.flex.client.global.FlexClient;

@Slf4j
public class FlexClientTest extends BaseServerTest {


    @Test
    public void test() {
        FlexClient flexClient = new FlexClient();
        GreetingService greetingService = flexClient.acquireInstance("flex-static://127.0.0.1:10200", GreetingService.class);
        String result = greetingService.hello("tom");
        log.info(result);
    }
}
