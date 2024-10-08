package plus.jdk.flex.server.listener;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

import plus.jdk.flex.server.config.ServerRegistry;

@Slf4j
public class VertxFlexServer implements IFlexServer {

    @Override
    public void startServer(ServerRegistry properties, Function<byte[], byte[]> handler) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(request -> request.bodyHandler((body) -> {
            byte[] result = handler.apply(body.getBytes());
            HttpServerResponse response = request.response();
            // Write to the response and end it
            response.end(Buffer.buffer(result));
        }));
        server.listen(properties.getPort());
    }
}
