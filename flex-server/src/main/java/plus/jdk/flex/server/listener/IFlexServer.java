package plus.jdk.flex.server.listener;

import plus.jdk.flex.server.config.ServerRegistry;

import java.util.function.Function;

public interface IFlexServer {

    /**
     * 启动服务器并传入指定参数
     * @param args 服务器启动所需的参数列表
     */
    void startServer(ServerRegistry properties, Function<byte[], byte[]> handler);
}
