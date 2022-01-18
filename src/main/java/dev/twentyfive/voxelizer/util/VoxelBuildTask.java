package dev.twentyfive.voxelizer.util;

import dev.twentyfive.voxelizer.math.Vector3;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class VoxelBuildTask extends BukkitRunnable {
    private final World world;
    private final Vector3 position;
    private final Voxel[] voxels;
    private final int taskIndex;
    private final int tasksTotal;

    public VoxelBuildTask(World world, Vector3 position, Voxel[] voxels, int taskIndex, int tasksTotal) {
        this.world = world;
        this.position = position;
        this.voxels = voxels;
        this.taskIndex = taskIndex;
        this.tasksTotal = tasksTotal;
    }

    @Override
    public void run() {
        System.out.println("Running build task " + taskIndex + " / " + tasksTotal + " (" + (taskIndex * 100 / tasksTotal) + "%)");

        for (Voxel voxel : this.voxels) {
            world.getBlockAt((int) (position.x + voxel.location.x), (int) (position.y + voxel.location.y), (int) (position.z + voxel.location.z))
                    .setType(voxel.material);
        }
    }
}
