package dev.twentyfive.voxelizer.util;

import dev.twentyfive.voxelizer.Voxelizer;
import dev.twentyfive.voxelizer.math.Geometry;
import dev.twentyfive.voxelizer.math.Vector3;
import dev.twentyfive.voxelizer.model.Model;
import dev.twentyfive.voxelizer.model.ModelParser;
import org.bukkit.Location;

import java.util.Arrays;

public class VoxelizeHelper {

    public static void loadVoxelizeAndBuildModelFromFile(String filename, double scale, double thickness, Location location) {
        Model model = loadModelFromFile(filename, scale);
        Voxel[] voxels = voxelizeModel(model, thickness);
        buildVoxels(voxels, location);
    }

    private static Model loadModelFromFile(String filename, double scale) {
        Logger.log();
        Logger.log("Loading model from file...");
        Logger.log("    File: " + filename);
        Logger.log("    Scale: " + scale);

        final long startTime = System.currentTimeMillis();

        final Model model = ModelParser.parseModelFromFile(filename, scale);

        final long deltaTime = System.currentTimeMillis() - startTime;

        Logger.log("    Model vertices: " + model.vertices.length);
        Logger.log("    Model triangles: " + model.triangles.length);
        Logger.log("    Model uvs: " + model.uvs.length);
        Logger.log("Loaded model in " + deltaTime + "ms");

        return model;
    }

    private static Voxel[] voxelizeModel(Model model, double thickness) {
        Logger.log();
        Logger.log("Voxelizing model...");

        final long startTime = System.currentTimeMillis();

        final Voxel[] voxels = Geometry.voxelizeModel(model, thickness).toArray(Voxel[]::new);

        final long deltaTime = System.currentTimeMillis() - startTime;

        Logger.log("    Voxels: " + voxels.length);
        Logger.log("Voxelized model in " + deltaTime + "ms");

        return voxels;
    }

    private static void buildVoxels(Voxel[] voxels, Location location) {
        final int MAX_BLOCKS_PER_TASK = 20000;
        final int RUN_TASK_EVERY_TICKS = 1;

        Logger.log();
        Logger.log("Spawning build tasks...");

        final long startTime = System.currentTimeMillis();

        int tasks = (int) Math.ceil(((float) voxels.length) / MAX_BLOCKS_PER_TASK);
        Logger.log("    Tasks: " + tasks);
        Logger.log("    Blocks / task: " + MAX_BLOCKS_PER_TASK);
        Logger.log("    Task delay: " + RUN_TASK_EVERY_TICKS);

        Vector3 position = new Vector3(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        for (int task = 0; task < tasks; task++) {
            Voxel[] taskVoxels = Arrays.copyOfRange(voxels, task * MAX_BLOCKS_PER_TASK, Math.min((task + 1) * MAX_BLOCKS_PER_TASK, voxels.length));
            String logMessage = "Running build task " + (task + 1) + " / " + tasks + " (" + ((task + 1) * 100 / tasks) + "%)";

            new VoxelBuildTask(location.getWorld(), position, taskVoxels, logMessage)
                    .runTaskLater(Voxelizer.instance, (long) (task + 1) * RUN_TASK_EVERY_TICKS);
        }

        final long deltaTime = System.currentTimeMillis() - startTime;
        Logger.log("Spawned build tasks in " + deltaTime + "ms");
        Logger.log();
    }

}
