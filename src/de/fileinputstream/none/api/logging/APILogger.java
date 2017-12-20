package de.fileinputstream.none.api.logging;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import de.fileinputstream.none.api.logging.enums.LogType;

public class APILogger {
	
	public APILogger() {
		
	}
	
	public void log(LogType type, String message) {
		Bukkit.getLogger().log(Level.SEVERE,"[" + type.toString() + " ]" , message);
	}

}
