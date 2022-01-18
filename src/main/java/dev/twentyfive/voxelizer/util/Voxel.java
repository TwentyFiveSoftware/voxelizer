package dev.twentyfive.voxelizer.util;

import dev.twentyfive.voxelizer.math.Vector3Int;
import org.bukkit.Material;

public class Voxel {
    public Vector3Int location;
    public Material material;

    public Voxel(Vector3Int location, Material material) {
        this.location = location;
        this.material = material;
    }
}
