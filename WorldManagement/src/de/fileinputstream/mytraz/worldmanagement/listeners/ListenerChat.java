package de.fileinputstream.mytraz.worldmanagement.listeners;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.chatlog.entry.ChatEntry;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 15.12.2018 at 22:43
 */
public class ListenerChat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player chatter = event.getPlayer();
        String chatterUUID = UUIDFetcher.getUUID(chatter.getName()).toString();
        if(Bootstrap.getInstance().getChatLogManager().chatLogs.containsKey(chatterUUID)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.GERMANY);
            Date date = new Date();
            dateFormat.format(date);
            final ArrayList<ChatEntry> currentChatEntries =Bootstrap.getInstance().getChatLogManager().chatLogs.get(chatterUUID);
            ChatEntry chatEntry = new ChatEntry(String.valueOf(currentChatEntries.size()+1),chatterUUID,date.toString(),event.getMessage(),chatter.getName());
            currentChatEntries.add(chatEntry);
            Bootstrap.getInstance().getChatLogManager().chatLogs.replace(chatterUUID,currentChatEntries);
        }

    }




}
