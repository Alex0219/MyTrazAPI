package de.fileinputstream.redisbuilder.servers.server;

import de.fileinputstream.redisbuilder.servers.enums.ServerState;

public class Server {

    String name;
    String group;
    String ip;
    int online;
    int maxOnline;
    ServerState serverState;

    public Server(String name, String group, String ip, int online, int maxOnline, ServerState serverState) {
        this.name = name;
        this.group = group;
        this.ip = ip;
        this.online = online;
        this.maxOnline = maxOnline;
        this.serverState = serverState;
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

    @Override
    public boolean equals(Object obj) {
        return (this == obj);
    }
}
