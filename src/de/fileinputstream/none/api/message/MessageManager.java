package de.fileinputstream.none.api.message;

import org.bukkit.entity.Player;

public class MessageManager {

    public String COMMAND_NOT_AVAILABLE = "§c§lDer Befehl ist nur für Spieler verfügbar.";

    public String COMMAND_NO_PERMISSION = "§c§lYou do not have permission to execute this command!";

	public MessageManager() {
		
	}
	
	public void sendMessage(Player player, String message) {
		player.sendMessage(message);
	}

}
