package de.fileinputstream.redisbuilder.networking.client;

public class NettyClient {

    public boolean connected;


    /**
     * Gibt zurück, ob der Client zu dem NettyServer verbunden ist.
     *
     * @return boolean
     */
    public boolean isConnected() {
        return connected;
    }
}
