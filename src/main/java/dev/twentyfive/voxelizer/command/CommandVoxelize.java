package dev.twentyfive.voxelizer.command;

import dev.twentyfive.voxelizer.util.VoxelizeHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CommandVoxelize implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player) || args.length < 3)
            return true;

        Location location = ((Player) sender).getLocation();
        double scale = Double.parseDouble(args[0]);
        double thickness = Double.parseDouble(args[1]);
        String filename = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).replaceAll("[^a-zA-Z0-9.-]", "_");

        VoxelizeHelper.loadVoxelizeAndBuildModelFromFile(filename, scale, thickness, location);

        return true;
    }

}
