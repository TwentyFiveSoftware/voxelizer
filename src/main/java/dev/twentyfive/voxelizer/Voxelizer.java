package dev.twentyfive.voxelizer;

import org.bukkit.plugin.java.JavaPlugin;

public class Voxelizer extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Voxelizer enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Voxelizer disabled");
    }
}
