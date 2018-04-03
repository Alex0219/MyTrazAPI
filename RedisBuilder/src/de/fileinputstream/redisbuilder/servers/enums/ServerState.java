package de.fileinputstream.redisbuilder.servers.enums;

public enum ServerState {


    WAITING("Warten"), INGAME("Im Spiel"),
    STOPPING("Am stoppen");

    String serverState;

    ServerState(String serverState) {
        this.serverState = serverState;
    }

    public String getServerState() {
        return serverState;
    }
}
