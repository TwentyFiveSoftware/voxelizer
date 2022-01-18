package dev.twentyfive.voxelizer.util;

import dev.twentyfive.voxelizer.math.Vector3Int;
import dev.twentyfive.voxelizer.model.Model;
import org.bukkit.Material;
import org.bukkit.World;

public class VoxelizeHelper {

    public static void buildVoxelizedModel(World world, Vector3Int position, Model model) {
        for (int y = position.y; y < position.y + 20; y++) {
            world.getBlockAt(position.x, y, position.z).setType(Material.RED_CONCRETE);
        }
    }

}
