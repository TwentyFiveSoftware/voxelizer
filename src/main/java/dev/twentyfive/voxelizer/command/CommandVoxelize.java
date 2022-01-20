package dev.twentyfive.voxelizer.command;

import dev.twentyfive.voxelizer.util.PathHelper;
import dev.twentyfive.voxelizer.util.VoxelizeHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandVoxelize implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp() || !sender.hasPermission("voxelizer.voxelize"))
            return true;

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        if (args.length == 0 || args.length > 3)
            return false;


        String filename = args[0].trim();
        double scale = 1;
        double thickness = 1;

        try {
            if (args.length >= 2)
                scale = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cThe scale parameter can't be parsed!");
            return false;
        }

        try {
            if (args.length >= 3)
                thickness = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cThe thickness parameter can't be parsed!");
            return false;
        }

        if (!PathHelper.isAllowedObjPath(filename)) {
            sender.sendMessage("§cForbidden characters in file path to .obj model!");
            return true;
        }

        if (!PathHelper.doesObjFileExist(filename)) {
            sender.sendMessage("§cSpecified .obj model not found!");
            return true;
        }

        Location location = ((Player) sender).getLocation();

        VoxelizeHelper.loadVoxelizeAndBuildModelFromFile(filename, scale, thickness, location);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return null;

        return switch (args.length) {
            case 0 -> PathHelper.getFilesInPath("");
            case 1 -> PathHelper.getFilesInPath(args[0]);
            case 2 -> new ArrayList<>(List.of("0.25", "0.5", "1"));
            case 3 -> new ArrayList<>(List.of("0.5", "1"));
            default -> new ArrayList<>();
        };
    }
}
