package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.BackupPerformData;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;


/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 18.12.2018 at 20:06
 */
public class CommandDoBackup implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();
            String rank = RankManager.getRank(uuid);
            if (rank.equalsIgnoreCase("admin") || rank.equalsIgnoreCase("sup") || rank.equalsIgnoreCase("mod") || rank.equalsIgnoreCase("partner")) {
                if (args.length == 0) {

                    if (!CommandRestoreBackup.backupList.containsKey(player)) {
                        player.sendMessage("§bAlex0219.de §7» §cDu hast noch keine Backup Aktion gestartet!");
                        return true;
                    }
                    BackupPerformData performData = CommandRestoreBackup.backupList.get(player);

                        player.sendMessage("§bAlex0219.de §7» §aStarte das Kopieren der Welt..");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        System.out.println(performData.getWorldID());
                        if (Bukkit.getWorld(performData.getWorldID()) != null) {
                            if (Bukkit.getWorld(performData.getWorldID()).getPlayers().size() > 0) {
                                for (Player players : Bukkit.getWorld(performData.getWorldID()).getPlayers()) {
                                    players.teleport(Bukkit.getWorld("world").getSpawnLocation());
                                    players.sendMessage("§bAlex0219.de §7» §aDu wurdest aus der Welt geworfen, da ein Backup geladen wird!");
                                }
                            }
                            unloadWorld(Bukkit.getWorld(performData.getWorldID()));
                        }


                        String path = new File(".").getAbsolutePath() + "/" + performData.getWorldID() + "/";
                        path = path.replace(".", "");
                        File worldFolder = new File(path);
                        System.out.println(worldFolder.getPath());
                        deleteWorld(worldFolder);

                        try {
                            ZipFile zipFile = new ZipFile(performData.getBackupPath());

                            zipFile.extractAll(Bootstrap.getInstance().getServer().getWorldContainer().getAbsolutePath());
                        } catch (ZipException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage("§bAlex0219.de §7» §aKonvertiere Welt..");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage("§bAlex0219.de §7» §aLade Welt..");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        player.sendMessage("§bAlex0219.de §7» §aWiederherstellen des Backups erfolgreich abgeschlossen!");

                    Bukkit.createWorld(new WorldCreator(performData.getWorldID()));

                    return true;
                } else {
                    player.sendMessage("§bAlex0219.de §7» Verwende /dobackup");
                    return true;
                }

            }
        } else {
            sender.sendMessage("§bAlex0219.de §7» Nur Spieler können diesen Befehl ausführen.");
        }
        return false;
    }

    private static void unloadWorld(World world) {
        if (!world.equals(null)) {

            Bukkit.getServer().unloadWorld(world, false);
            System.out.println(world.getName() + " unloaded!");
        }
    }

    private static boolean deleteWorld(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
}
