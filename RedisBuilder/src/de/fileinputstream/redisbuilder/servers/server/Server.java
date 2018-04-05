package de.fileinputstream.redisbuilder.servers.server;

import de.fileinputstream.redisbuilder.servers.ChannelObject;
import de.fileinputstream.redisbuilder.servers.enums.ServerState;
import io.netty.channel.Channel;

public class Server {

    String name;
    String group;
    String ip;
    int online;
    int maxOnline;
    ServerState serverState;
    ChannelObject channelObject;

    public Server(String name, String group, String ip, int online, int maxOnline, ServerState serverState, ChannelObject channelObjectl) {
        this.name = name;
        this.group = group;
        this.ip = ip;
        this.online = online;
        this.maxOnline = maxOnline;
        this.serverState = serverState;
        this.channelObject = channelObject;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getIp() {
        return ip;
    }

    public void stop() {

    }

    public int getMaxOnline() {
        return maxOnline;
    }

    public String getServerState() {
        return serverState.getServerState();
    }

    public int getOnline() {
        return online;
    }

    public ChannelObject getChannelObject() {
        return channelObject;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Channel getChannel() {
        return channelObject.getChannel();
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj);
    }
}
