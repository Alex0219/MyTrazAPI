package de.fileinputstream.mytraz.api.logging;

import de.fileinputstream.mytraz.api.logging.enums.LogType;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class APILogger {
	
	public APILogger() {
		
	}
	
	public void log(LogType type, String message) {
		Bukkit.getLogger().log(Level.SEVERE,"[" + type.toString() + " ]" , message);
	}

}
