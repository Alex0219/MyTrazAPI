package de.fileinputstream.none.api.anticheat.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Alexander on 23.07.2017.
 */
public class NettyClient {

    private static final int port = 23580;
    private static final boolean KEEPALIVE = true;
    public static void startClient() {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);

        bootstrap.option(ChannelOption.SO_KEEPALIVE, KEEPALIVE);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                System.out.println("Verbindung zum AntiCheat-Log Server wurde aufgebaut.");

            }
        });

    }

    public static void sendMessageToServer(Object message) {

    }
}
