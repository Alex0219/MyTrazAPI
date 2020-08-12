package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Alexander on 08.06.2020 21:16
 * © 2020 Alexander Fiedler
 */
public class CommandSethome implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            DBUser dbUser = new DBUser(((Player) commandSender).getUniqueId().toString(), commandSender.getName());

            if (args.length == 0) {
                if (Bootstrap.getInstance().getWorldManager().hasWorld(dbUser.getUuid())) {
                    World world = Bukkit.getWorld(Bootstrap.getInstance().getWorldManager().getWorld(dbUser.getUuid()));
                    if (world == null) {

                    }
                    if (!Bootstrap.getInstance().getWorldManager().worldExists(player.getWorld().getName())) {
                        player.sendMessage("§bMC-Survival.de §7» Du darfst dir in dieser Welt kein Home setzen!");
                        return true;
                    }
                    if (!player.getWorld().getName().equalsIgnoreCase(world.getName())) {
                        player.sendMessage("§bMC-Survival.de §7» Du darfst dir in dieser Welt kein Home setzen!");
                        return true;

                    } else if (Bootstrap.getInstance().getWorldManager().isResidentInWorld(dbUser.getUuid(), world.getName())) {

                        Location location = player.getLocation();

                        Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.x", String.valueOf(location.getX()));
                        Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.y", String.valueOf(location.getY()));
                        Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.z", String.valueOf(location.getZ()));
                        Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.pitch", String.valueOf(location.getPitch()));
                        Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.yaw", String.valueOf(location.getYaw()));
                        Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.world", String.valueOf(location.getWorld().getName()));

                        player.sendMessage("§bMC-Survival.de §7» Du hast Home §c#1 §7gesetzt!");
                        return true;
                    } else if (player.getWorld().getName().equalsIgnoreCase(world.getName())) {
                        Location location = player.getLocation();

                        Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.x", String.valueOf(location.getX()));
                        Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.y", String.valueOf(location.getY()));
                        Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.z", String.valueOf(location.getZ()));
                        Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.pitch", String.valueOf(location.getPitch()));
                        Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.yaw", String.valueOf(location.getYaw()));
                        Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.world", String.valueOf(location.getWorld().getName()));

                        player.sendMessage("§bMC-Survival.de §7» Du hast Home §c#1 §7gesetzt!");
                        return true;
                    }

                } else {
                    player.sendMessage("§bMC-Survival.de §7» Du hast noch keine Welt!");
                }
            }
        }
        return false;
    }
}
