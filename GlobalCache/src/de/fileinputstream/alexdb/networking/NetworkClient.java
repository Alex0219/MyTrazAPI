package de.fileinputstream.alexdb.networking;

import de.fileinputstream.mcms.master.ssl.SSLFactory;
import de.fileinputstream.mcms.wrapper.network.cypt.PacketDecoder;
import de.fileinputstream.mcms.wrapper.network.cypt.PacketEncoder;
import de.fileinputstream.mcms.wrapper.network.handler.GameServerHandler;
import de.fileinputstream.mcms.wrapper.network.handler.MasterHandler;
import de.fileinputstream.mcms.wrapper.network.packet.Packet;
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
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class NetworkClient {

    public Channel channel;
    public BlockingQueue<Packet> queue = new LinkedBlockingDeque<>();
    String host;
    int port;


    public NetworkClient(String host, int port) {
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
                        protected void initChannel(final SocketChannel socketChannel) {

                            final SSLContext sslContext = SSLFactory.createAndInitSSLContext("client.jks");
                            final SSLEngine sslEngine = sslContext.createSSLEngine();
                            sslEngine.setEnabledCipherSuites(sslContext.getSocketFactory().getSupportedCipherSuites());
                            sslEngine.setUseClientMode(true);
                            socketChannel.pipeline().addLast("ssl", new SslHandler(sslEngine));
                            socketChannel.pipeline().addLast(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
                            socketChannel.pipeline().addLast(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
                            socketChannel.pipeline().addLast("encoder", new PacketEncoder());
                            socketChannel.pipeline().addLast("decoder", new PacketDecoder());
                            socketChannel.pipeline().addLast("gameServerHandler", new GameServerHandler());
                            socketChannel.pipeline().addLast("handler", new MasterHandler());


                        }
                    });
            ChannelFuture f = bootstrap.connect(host, port).sync();
            f.channel().closeFuture().sync();

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

}
