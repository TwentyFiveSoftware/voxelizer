package dev.twentyfive.voxelizer;

import dev.twentyfive.voxelizer.command.CommandVoxelize;
import dev.twentyfive.voxelizer.util.PathHelper;
import org.bukkit.plugin.java.JavaPlugin;

public class Voxelizer extends JavaPlugin {

    public static Voxelizer instance;

    @Override
    public void onEnable() {
        instance = this;

        PathHelper.createDataFolder();

        this.getCommand("voxelize").setExecutor(new CommandVoxelize());
        this.getCommand("voxelize").setTabCompleter(new CommandVoxelize());
    }

}
