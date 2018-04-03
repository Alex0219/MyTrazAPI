package de.fileinputstream.redisbuilder.servers.server;

import java.util.ArrayList;

public class ServerRegistry {

    public ArrayList<Server> servers = new ArrayList<>();

    public void addServer(Server server) {
        if (!servers.contains(server)) {
            servers.add(server);
            System.out.println("Added serrver: " + server.getName());

        } else {
            System.out.println("Tried to add already existing server:" + server.getName() + ". This error should never happen! Please report!");
        }

    }
}
