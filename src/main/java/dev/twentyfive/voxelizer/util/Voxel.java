package dev.twentyfive.voxelizer.util;

import dev.twentyfive.voxelizer.math.Vector3;
import org.bukkit.Material;

public class Voxel {
    public Vector3 location;
    public Material material;

    public Voxel(Vector3 location, Material material) {
        this.location = location;
        this.material = material;
    }
}
