package de.fileinputstream.redisbuilder.networking.client;

import de.fileinputstream.redisbuilder.networking.Packet;
import de.fileinputstream.redisbuilder.networking.codec.PacketDecoder;
import de.fileinputstream.redisbuilder.networking.codec.PacketEncoder;
import de.fileinputstream.redisbuilder.networking.handler.WrapperHandler;
import de.fileinputstream.redisbuilder.networking.ssl.SSLFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class NettyClient {

    public boolean connected;


    /**
     * Gibt zurück, ob der Client zu dem NettyServer verbunden ist.
     *
     * @return boolean
     */

    String host;
    int port;
    public Channel channel;
    public BlockingQueue<Packet> queue = new LinkedBlockingDeque<>();


    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public void run() {
        boolean epoll = Epoll.isAvailable();
        System.out.println("[Sys] Server-Typ: " + (epoll ? "Epoll" : "Nio"));
        EventLoopGroup mainEventLoopGroup = epoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();


        try {
            bootstrap.group(mainEventLoopGroup)
                    .channel(epoll ? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(final SocketChannel socketChannel) throws Exception {
                            final SSLContext sslContext = SSLFactory.createAndInitSSLContext("client.jks");
                            final SSLEngine sslEngine = sslContext.createSSLEngine();
                            sslEngine.setEnabledCipherSuites(sslContext.getSocketFactory().getSupportedCipherSuites());
                            sslEngine.setUseClientMode(true);
                            socketChannel.pipeline().addLast("ssl", new SslHandler(sslEngine));
                            socketChannel.pipeline().addLast("encoder", new PacketEncoder());
                            socketChannel.pipeline().addLast("decoder", new PacketDecoder());
                            socketChannel.pipeline().addLast("handler", new WrapperHandler());

                        }
                    });
            ChannelFuture f = bootstrap.connect(host, port).sync();
            f.channel().closeFuture().sync();
            System.out.println("Succesfully established connection to the wrapper server!");

            this.channel = f.channel();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mainEventLoopGroup.shutdownGracefully();

        }
    }




    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Channel getChannel() {
        return channel;
    }
    public boolean isConnected() {
        return connected;
    }
}
