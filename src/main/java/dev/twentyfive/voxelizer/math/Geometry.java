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

    private static Vector3 projectVector3To2DPlaneCoordinates(Vector3 planeNormal, Vector3 planePointA, Vector3 planePointB, Vector3 point) {
        Vector3 xAxis = Vector3.sub(planePointB, planePointA).normalize();
        Vector3 yAxis = Vector3.crossProduct(planeNormal, xAxis).normalize();
        Vector3 vectorToPoint = Vector3.sub(point, planePointA);
        return new Vector3(Vector3.dotProduct(vectorToPoint, xAxis), Vector3.dotProduct(vectorToPoint, yAxis), 0);
    }


    public static ArrayList<Voxel> getVoxelizedModel(Model model, double thickness) {
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
            Vector3 projectedA = projectVector3To2DPlaneCoordinates(planeNormal, a, b, a);
            Vector3 projectedB = projectVector3To2DPlaneCoordinates(planeNormal, a, b, b);
            Vector3 projectedC = projectVector3To2DPlaneCoordinates(planeNormal, a, b, c);

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Vector3 point = new Vector3(x, y, z);

                        double distance = Math.abs(calculateSignedDistancePlanePoint(planeNormal, a, point));
                        if (distance > thickness)
                            continue;

                        Vector3 projectedPoint = projectVector3To2DPlaneCoordinates(planeNormal, a, b, projectPointOnPlane(planeNormal, a, point));
                        boolean insideTriangle = edgeFunction(projectedB, projectedA, projectedPoint) && edgeFunction(projectedC, projectedB, projectedPoint)
                                && edgeFunction(projectedA, projectedC, projectedPoint);
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
