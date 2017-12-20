package de.fileinputstream.none.api.resilentclient;

import com.blogspot.debukkitsblog.net.Client;
import com.blogspot.debukkitsblog.net.Server;

import de.fileinputstream.none.api.Bootstrap;

public class ResilentClient extends Client {

	public boolean alreadyPrinted;
	
	public ResilentClient(String hostname, int port) {
		super(hostname, port);
	}
	
	@Override
	public void onConnectionGood() {
		
		Bootstrap.getInstance().isConnected = true;
			System.out.println("Succesfully established connection to bungeecord.");
	}
	
	@Override
	public void onConnectionProblem() {
		System.out.println("Could not connect to bungeecord!");
	}
	
}
