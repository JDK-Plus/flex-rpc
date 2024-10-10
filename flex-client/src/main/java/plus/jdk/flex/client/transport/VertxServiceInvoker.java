package plus.jdk.flex.client.transport;

import plus.jdk.flex.client.config.ClientRegistry;
import plus.jdk.flex.common.global.FlexStatus;
import plus.jdk.flex.common.global.FlexRpcException;
import plus.jdk.flex.common.model.RequestContext;

import java.util.concurrent.CountDownLatch;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import lombok.extern.slf4j.Slf4j;
import plus.jdk.flex.common.model.ServiceInstance;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class VertxServiceInvoker implements IServiceInvoker {

    @Override
    public byte[] invokeMessage(RequestContext context, byte[] body) {
        ServiceInstance instance = context.getServiceInstance();
        WebClientOptions options = new WebClientOptions();
        options.setKeepAlive(true);
        WebClient client = WebClient.create(Vertx.vertx(), options);
        HttpRequest<Buffer> httpRequest =
                client.get(instance.getPort(), instance.getAddress(), instance.getPath()).method(HttpMethod.POST);
        httpRequest.connectTimeout(context.getConnectTimeout());
        httpRequest.idleTimeout(context.getSocketTimeout());
        AtomicReference<byte[]> result = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        httpRequest.sendBuffer(Buffer.buffer(body)).onSuccess((response) -> {
            Buffer buffer = response.body();
            result.set(buffer.getBytes());
            latch.countDown();
        });
        try{
            if(!latch.await(context.getSocketTimeout(), TimeUnit.MILLISECONDS)) {
                throw new FlexRpcException(FlexStatus.read_time_out);
            }
        }catch(Exception e){
            throw new FlexRpcException(FlexStatus.read_time_out);
        }
        return result.get();
    }
}
