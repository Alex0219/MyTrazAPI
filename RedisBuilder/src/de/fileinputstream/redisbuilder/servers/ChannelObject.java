package de.fileinputstream.redisbuilder.servers;

import io.netty.channel.Channel;

public class ChannelObject {

    public ChannelObject(Channel channel) {
        this.channel = channel;
    }
    public Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
