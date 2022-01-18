package dev.twentyfive.voxelizer.util;

import dev.twentyfive.voxelizer.math.Geometry;
import dev.twentyfive.voxelizer.math.Vector3Int;
import dev.twentyfive.voxelizer.model.Model;
import org.bukkit.World;

public class VoxelizeHelper {

    public static void buildVoxelizedModel(World world, Vector3Int position, Model model) {
        for (Voxel voxel : Geometry.getVoxelizedModel(model)) {
            world.getBlockAt(position.x + voxel.location.x, position.y + voxel.location.y, position.z + voxel.location.z)
                    .setType(voxel.material);
        }
    }

}
