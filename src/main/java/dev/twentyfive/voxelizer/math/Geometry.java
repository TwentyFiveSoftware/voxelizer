package dev.twentyfive.voxelizer.math;

import dev.twentyfive.voxelizer.model.Model;
import dev.twentyfive.voxelizer.util.Voxel;
import org.bukkit.Material;

import java.util.ArrayList;

public class Geometry {

    private static Vector3 calculateNormalizedPlaneNormal(Vector3 a, Vector3 b, Vector3 c) {
        return Vector3.crossProduct(Vector3.sub(b, a), Vector3.sub(c, a)).normalize();
    }

    private static double calculateSignedDistancePlanePoint(Vector3 planeNormal, Vector3 planePoint, Vector3 point) {
        return Vector3.dotProduct(planeNormal, (Vector3.sub(point, planePoint)));
    }

    private static Vector3 projectPointOnPlane(Vector3 planeNormal, Vector3 planePoint, Vector3 point) {
        return Vector3.add(point, Vector3.multiply(planeNormal, -calculateSignedDistancePlanePoint(planeNormal, planePoint, point)));
    }

    private static boolean edgeFunction(Vector3 a, Vector3 b, Vector3 point) {
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
            int minZ = (int) Math.min(a.z, Math.min(b.z, c.z));

            int maxX = (int) Math.max(a.x, Math.max(b.x, c.x));
            int maxY = (int) Math.max(a.y, Math.max(b.y, c.y));
            int maxZ = (int) Math.max(a.z, Math.max(b.z, c.z));

            Vector3 planeNormal = calculateNormalizedPlaneNormal(a, b, c);

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Vector3 point = new Vector3(x, y, z);

                        double distance = Math.abs(calculateSignedDistancePlanePoint(planeNormal, a, point));
                        if (distance > 0.5)
                            continue;

                        Vector3 projectedPoint = projectPointOnPlane(planeNormal, a, point);
                        boolean insideTriangle = edgeFunction(a, b, projectedPoint) && edgeFunction(b, c, projectedPoint) && edgeFunction(c, a, projectedPoint);
                        if (!insideTriangle)
                            continue;

                        voxels.add(new Voxel(new Vector3Int(x, y, z), Material.RED_CONCRETE));
                    }
                }
            }
        }

        return voxels;
    }
}
