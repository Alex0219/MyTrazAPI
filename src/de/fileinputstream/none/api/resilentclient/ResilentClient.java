package de.fileinputstream.none.api.resilentclient;

import com.blogspot.debukkitsblog.net.Client;


import de.fileinputstream.none.api.Bootstrap;

public class ResilentClient extends Client {

	public boolean alreadyPrinted;
	
	public ResilentClient(String hostname, int port) {
		super(hostname, port);
	}
	
	@Override
	public void onConnectionGood() {
		
		Bootstrap.getInstance().isConnected = true;
        System.out.println("Backend -> Succesfully established connection to bungeecord.");
    }

    @Override
	public void onConnectionProblem() {
        System.out.println("Backend -> Could not connect to bungeecord!");
    }
}
