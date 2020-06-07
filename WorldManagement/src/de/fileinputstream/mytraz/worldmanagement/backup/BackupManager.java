package de.fileinputstream.mytraz.worldmanagement.backup;

import de.fileinputstream.mytraz.worldmanagement.backup.compression.ZipUtils;


import org.bukkit.World;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 18.12.2018 at 16:33
 */
public class BackupManager {

    private ZipUtils zipUtils;

    public BackupManager() {
        zipUtils = new ZipUtils();
    }

    public void performBackup(BackupData backupData) {
        Bukkit.getLogger().log(Level.INFO, "Starting backup of world: " + backupData.getWorldID());
        File worldFolder = new File(Bukkit.getServer().getWorldContainer(), backupData.getWorldID());
        final long currentMillis = System.currentTimeMillis();
        if (getZipUtils().zipFolder(worldFolder.getAbsolutePath(), "plugins/WorldManagement/worldBackups/" + backupData.getWorldID() + "_" + currentMillis + ".zip")) {
            Bukkit.getLogger().log(Level.INFO, "Finished world backup of world: " + backupData.getWorldID());
        } else {
            Bukkit.getLogger().log(Level.WARNING, "World backup failed for world: " + backupData.getWorldID());
        }

    }

    private ZipUtils getZipUtils() {
        return zipUtils;
    }

    public List<String> getBackupsFromWorld(String worldID) {
        File folder = new File("plugins/WorldManagement/worldBackups/");
        File[] files = folder.listFiles();
        List<String> worldBackups = new ArrayList<>();
        for (File file : files) {
            if (file.getName().startsWith(worldID + "_")) {
                worldBackups.add(file.getName());
            }
        }
        return worldBackups;

    }

    public void runCronJob() {
        for (World worlds : Bukkit.getWorlds()) {

        }
    }
}
