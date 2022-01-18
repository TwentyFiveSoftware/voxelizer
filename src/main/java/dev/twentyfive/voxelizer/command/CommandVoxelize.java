package dev.twentyfive.voxelizer.command;

import dev.twentyfive.voxelizer.math.Vector3Int;
import dev.twentyfive.voxelizer.model.Model;
import dev.twentyfive.voxelizer.model.ModelParser;
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
        Vector3Int pos = new Vector3Int(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        double scale = Double.parseDouble(args[0]);
        double thickness = Double.parseDouble(args[1]);

        String filename = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).replaceAll("[^a-zA-Z0-9.-]", "_");
        System.out.println("Trying to parse model: " + filename);

        System.out.println("Parsing model...");
        long startTimeParsing = System.currentTimeMillis();
        Model model = ModelParser.parseModelFromFile(filename, scale);
        long deltaTimeParsing = System.currentTimeMillis() - startTimeParsing;
        System.out.println("Parsed model in " + deltaTimeParsing + "ms");

        System.out.println("    Model vertices: " + model.vertices.length);
        System.out.println("    Model triangles: " + model.triangles.length);

        System.out.println("Building model...");
        long startTimeBuilding = System.currentTimeMillis();
        VoxelizeHelper.buildVoxelizedModel(location.getWorld(), pos, model, thickness);
        long deltaTimeBuilding = System.currentTimeMillis() - startTimeBuilding;
        System.out.println("Built model in " + deltaTimeBuilding + "ms");
        System.out.println("Completed!");

        return true;
    }

}
