package dev.twentyfive.voxelizer.math;

import dev.twentyfive.voxelizer.model.Model;
import dev.twentyfive.voxelizer.util.Voxel;
import org.bukkit.Material;

import java.util.ArrayList;

public class Geometry {

    static boolean edgeFunction(Vector3 a, Vector3 b, Vector3 point) {
        return (point.x - a.x) * (b.y - a.y) - (point.y - a.y) * (b.x - a.x) >= 0;
    }

    public static ArrayList<Voxel> getVoxelizedModel(Model model) {
        ArrayList<Voxel> voxels = new ArrayList<>();

        for (Triangle triangle : model.triangles) {
            Vector3 a = model.vertices[triangle.a];
            Vector3 b = model.vertices[triangle.b];
            Vector3 c = model.vertices[triangle.c];

            int minX = (int) Math.min(a.x, Math.min(b.x, c.x));
            int minY = (int) Math.min(a.y, Math.min(b.y, c.y));
            int maxX = (int) Math.max(a.x, Math.max(b.x, c.x));
            int maxY = (int) Math.max(a.y, Math.max(b.y, c.y));

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    Vector3 point = new Vector3(x, y, 0);
                    boolean inside = edgeFunction(a, b, point) && edgeFunction(b, c, point) && edgeFunction(c, a, point);

                    if (inside)
                        voxels.add(new Voxel(new Vector3Int(x, 0, y), Material.RED_CONCRETE));
                }
            }
        }

        return voxels;
    }
}
