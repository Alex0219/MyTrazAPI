package de.fileinputstream.mytraz.bungee.networking;

import de.fileinputstream.mytraz.bungee.networking.codec.PacketDecoder;
import de.fileinputstream.mytraz.bungee.networking.codec.PacketEncoder;
import de.fileinputstream.mytraz.bungee.networking.handler.GameServerHandler;
import de.fileinputstream.mytraz.bungee.networking.handler.WrapperHandler;
import de.fileinputstream.mytraz.bungee.networking.ssl.SSLFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

public class NettyServer {

    public boolean connected;

    String host;
    int port;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public void run() {
        connected = false;
        boolean epoll = Epoll.isAvailable();
        System.out.println("[Sys] Server-Typ: " + (epoll ? "Epoll" : "Nio"));
        EventLoopGroup mainEventLoopGroup = epoll ? new EpollEventLoopGroup(2) : new NioEventLoopGroup(2);
        EventLoopGroup workerEventLoopGroup = epoll ? new EpollEventLoopGroup(2) : new NioEventLoopGroup(2);
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(mainEventLoopGroup, workerEventLoopGroup)
                    .channel(epoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(final SocketChannel socketChannel) throws Exception {
                            final SSLContext sslContext = SSLFactory.createAndInitSSLContext("client.jks");
                            final SSLEngine sslEngine = sslContext.createSSLEngine();
                            sslEngine.setUseClientMode(false);
                            sslEngine.setEnabledCipherSuites(sslContext.getSocketFactory().getSupportedCipherSuites());

                            socketChannel.pipeline().addLast("ssl", new SslHandler(sslEngine));
                            //  socketChannel.pipeline().addLast(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
                            //socketChannel.pipeline().addLast(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
                            socketChannel.pipeline().addLast("decoder", new PacketDecoder());
                            socketChannel.pipeline().addLast("encoder", new PacketEncoder());
                            socketChannel.pipeline().addLast("handler", new GameServerHandler());
                        }
                    });
            serverBootstrap.bind(port).channel().closeFuture().sync();
            connected = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mainEventLoopGroup.shutdownGracefully();
            workerEventLoopGroup.shutdownGracefully();
        }
    }


    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
