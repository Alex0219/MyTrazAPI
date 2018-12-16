package de.fileinputstream.mytraz.worldmanagement.chatlog;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.chatlog.entry.ChatEntry;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 15.12.2018 at 22:40
 */
public class ChatLogManager {

    public HashMap<String, ArrayList<ChatEntry>> chatLogs = new HashMap<>();

    public ArrayList<String> alreadyLogged = new ArrayList<>();
    public String createChatLog(final String uuid,String creator, String target) {
        if(!alreadyLogged.contains(target)) {
            final String chatLogID = UUID.randomUUID().toString();
            System.out.println("Creating a new chatlog...");
            ArrayList<ChatEntry> chatEntries = chatLogs.get(uuid);
            if(chatEntries.size() > 0) {
                Bootstrap.getInstance().getJedis().hset("chatlog:"+chatLogID,"creator",creator);
                Bootstrap.getInstance().getJedis().hset("chatlog:"+chatLogID,"target",target);
                for(ChatEntry listEntries : chatEntries) {
                    Bootstrap.getInstance().getJedis().hset("chatlog:"+chatLogID,listEntries.getId(),listEntries.getMessage()+","+listEntries.getTimestamp()+","+listEntries.getUuid() + ","+listEntries.getUsername());
                }
                alreadyLogged.add(target);
                removeLogged(target,uuid);
                return chatLogID;
            } else {
                return "NOMESSAGES";
            }
        } else {
            return "ALREADYLOGGED";
        }


    }

    public void removeLogged(String name, String uuid) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Bootstrap.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                if(alreadyLogged.contains(name)) {
                    alreadyLogged.remove(name);
                }
                if(chatLogs.containsKey(uuid)) {
                    chatLogs.remove(uuid);
                }
            }
        },6000L);
    }

}
