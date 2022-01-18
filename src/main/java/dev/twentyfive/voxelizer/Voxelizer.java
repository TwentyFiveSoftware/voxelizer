package dev.twentyfive.voxelizer;

import dev.twentyfive.voxelizer.command.CommandVoxelize;
import org.bukkit.plugin.java.JavaPlugin;

public class Voxelizer extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("voxelize").setExecutor(new CommandVoxelize());

        getLogger().info("Voxelizer enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Voxelizer disabled");
    }
}
