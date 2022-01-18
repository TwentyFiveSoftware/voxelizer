package dev.twentyfive.voxelizer.command;

import dev.twentyfive.voxelizer.math.Vector3Int;
import dev.twentyfive.voxelizer.model.Models;
import dev.twentyfive.voxelizer.util.VoxelizeHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandVoxelize implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return true;

        Location location = ((Player) sender).getLocation();
        Vector3Int pos = new Vector3Int(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        VoxelizeHelper.buildVoxelizedModel(location.getWorld(), pos, Models.TEST_TRIANGLE.model);

        return true;
    }

}
