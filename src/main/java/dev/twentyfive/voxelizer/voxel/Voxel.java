package dev.twentyfive.voxelizer.voxel;

import dev.twentyfive.voxelizer.math.Vector3;
import org.bukkit.Material;

public class Voxel {

    public final Vector3 location;
    public final Material material;

    public Voxel(Vector3 location, Material material) {
        this.location = location;
        this.material = material;
    }

}
