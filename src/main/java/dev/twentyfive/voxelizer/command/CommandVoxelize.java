package dev.twentyfive.voxelizer.command;

import dev.twentyfive.voxelizer.math.Vector3Int;
import dev.twentyfive.voxelizer.model.Model;
import dev.twentyfive.voxelizer.model.ModelParser;
import dev.twentyfive.voxelizer.model.Models;
import dev.twentyfive.voxelizer.util.VoxelizeHelper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystems;
import java.nio.file.Paths;

public class CommandVoxelize implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return true;

        Location location = ((Player) sender).getLocation();
        Vector3Int pos = new Vector3Int(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        Model model = Models.TEST_TRIANGLE.model;
        if (args.length > 0) {
            String path = Paths.get("").toAbsolutePath()
                    + FileSystems.getDefault().getSeparator()
                    + "plugins"
                    + FileSystems.getDefault().getSeparator()
                    + "Voxelizer"
                    + FileSystems.getDefault().getSeparator()
                    + String.join(" ", args);

            System.out.println("Trying to parse model at: " + path);

            model = ModelParser.parseModelFromFile(path);
            System.out.println("Parsing model...");
        }

        System.out.println("Model vertices: " + model.vertices.length);
        System.out.println("Model triangles: " + model.triangles.length);

        System.out.println("Building model...");
        VoxelizeHelper.buildVoxelizedModel(location.getWorld(), pos, model);
        System.out.println("Completed");

        return true;
    }

}
