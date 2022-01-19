package dev.twentyfive.voxelizer;

import dev.twentyfive.voxelizer.command.CommandVoxelize;
import org.bukkit.plugin.java.JavaPlugin;

public class Voxelizer extends JavaPlugin {

    public static Voxelizer instance;

    @Override
    public void onEnable() {
        instance = this;
        this.getCommand("voxelize").setExecutor(new CommandVoxelize());
    }

}
