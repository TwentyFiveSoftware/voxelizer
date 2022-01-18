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
        System.out.println();
        System.out.println("Loading model from file...");
        System.out.println("    File: " + filename);
        System.out.println("    Scale: " + scale);

        final long startTime = System.currentTimeMillis();

        final Model model = ModelParser.parseModelFromFile(filename, scale);

        final long deltaTime = System.currentTimeMillis() - startTime;

        System.out.println("    Model vertices: " + model.vertices.length);
        System.out.println("    Model triangles: " + model.triangles.length);
        System.out.println("Loading model in " + deltaTime + "ms");

        return model;
    }

    private static Voxel[] voxelizeModel(Model model, double thickness) {
        System.out.println();
        System.out.println("Voxelizing model...");

        final long startTime = System.currentTimeMillis();

        final Voxel[] voxels = Geometry.voxelizeModel(model, thickness).toArray(Voxel[]::new);

        final long deltaTime = System.currentTimeMillis() - startTime;

        System.out.println("    Voxels: " + voxels.length);
        System.out.println("Voxelized model in " + deltaTime + "ms");

        return voxels;
    }

    private static void buildVoxels(Voxel[] voxels, Location location) {
        final int MAX_BLOCKS_PER_TASK = 20000;
        final int RUN_TASK_EVERY_TICKS = 1;

        System.out.println();
        System.out.println("Spawning build tasks...");

        final long startTime = System.currentTimeMillis();

        int tasks = (int) Math.ceil(((float) voxels.length) / MAX_BLOCKS_PER_TASK);
        System.out.println("    Tasks: " + tasks);
        System.out.println("    Blocks / task: " + MAX_BLOCKS_PER_TASK);
        System.out.println("    Task delay: " + RUN_TASK_EVERY_TICKS);

        Vector3 position = new Vector3(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        for (int task = 0; task < tasks; task++) {
            Voxel[] taskVoxels = Arrays.copyOfRange(voxels, task * MAX_BLOCKS_PER_TASK, Math.min((task + 1) * MAX_BLOCKS_PER_TASK, voxels.length));
            new VoxelBuildTask(location.getWorld(), position, taskVoxels, task + 1, tasks)
                    .runTaskLater(Voxelizer.instance, (long) (task + 1) * RUN_TASK_EVERY_TICKS);
        }

        final long deltaTime = System.currentTimeMillis() - startTime;
        System.out.println("Spawned build tasks in " + deltaTime + "ms");
        System.out.println();
    }

}
