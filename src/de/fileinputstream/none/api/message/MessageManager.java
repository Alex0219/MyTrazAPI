package de.fileinputstream.none.api.message;

import org.bukkit.entity.Player;

public class MessageManager {
	
	public MessageManager() {
		
	}
	
	public void sendMessage(Player player, String message) {
		player.sendMessage(message);
	}

}
