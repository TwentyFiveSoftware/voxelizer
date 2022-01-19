package dev.twentyfive.voxelizer.math;

import dev.twentyfive.voxelizer.model.Model;
import dev.twentyfive.voxelizer.model.ModelMaterial;
import dev.twentyfive.voxelizer.model.VoxelMaterial;
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

    private static double edgeFunction(Vector3 a, Vector3 b, Vector3 c) {
        return (c.x - a.x) * (b.y - a.y) - (c.y - a.y) * (b.x - a.x);
    }

    private static Vector3 projectVector3To2DPlaneCoordinates(Vector3 planeNormal, Vector3 planePointA, Vector3 planePointB, Vector3 point) {
        Vector3 xAxis = Vector3.sub(planePointB, planePointA).normalize();
        Vector3 yAxis = Vector3.crossProduct(planeNormal, xAxis).normalize();
        Vector3 vectorToPoint = Vector3.sub(point, planePointA);
        return new Vector3(Vector3.dotProduct(vectorToPoint, xAxis), Vector3.dotProduct(vectorToPoint, yAxis), 0);
    }

    public static ArrayList<Voxel> voxelizeModel(Model model, double thickness) {
        ArrayList<Voxel> voxels = new ArrayList<>();

        for (Triangle triangle : model.triangles) {
            Vector3 a = model.vertices[triangle.a];
            Vector3 b = model.vertices[triangle.b];
            Vector3 c = model.vertices[triangle.c];

            int minX = (int) (Math.min(a.x, Math.min(b.x, c.x)) - Math.ceil(thickness));
            int minY = (int) (Math.min(a.y, Math.min(b.y, c.y)) - Math.ceil(thickness));
            int minZ = (int) (Math.min(a.z, Math.min(b.z, c.z)) - Math.ceil(thickness));

            int maxX = (int) (Math.max(a.x, Math.max(b.x, c.x)) + Math.ceil(thickness));
            int maxY = (int) (Math.max(a.y, Math.max(b.y, c.y)) + Math.ceil(thickness));
            int maxZ = (int) (Math.max(a.z, Math.max(b.z, c.z)) + Math.ceil(thickness));

            Vector3 planeNormal = calculateNormalizedPlaneNormal(a, b, c);
            Vector3 projectedA = projectVector3To2DPlaneCoordinates(planeNormal, a, b, a);
            Vector3 projectedB = projectVector3To2DPlaneCoordinates(planeNormal, a, b, b);
            Vector3 projectedC = projectVector3To2DPlaneCoordinates(planeNormal, a, b, c);

            Vector3 aUV = (triangle.aUV >= 0 && model.uvs.length > triangle.aUV) ? model.uvs[triangle.aUV] : Vector3.zero();
            Vector3 bUV = (triangle.bUV >= 0 && model.uvs.length > triangle.bUV) ? model.uvs[triangle.bUV] : Vector3.zero();
            Vector3 cUV = (triangle.cUV >= 0 && model.uvs.length > triangle.cUV) ? model.uvs[triangle.cUV] : Vector3.zero();

            ModelMaterial modelMaterial = triangle.material;


            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Vector3 point = new Vector3(x + 0.5, y + 0.5, z + 0.5);

                        double distance = Math.abs(calculateSignedDistancePlanePoint(planeNormal, a, point));
                        if (distance > thickness)
                            continue;

                        Vector3 projectedPoint = projectVector3To2DPlaneCoordinates(planeNormal, a, b, projectPointOnPlane(planeNormal, a, point));
                        double wA = edgeFunction(projectedC, projectedB, projectedPoint);
                        double wB = edgeFunction(projectedA, projectedC, projectedPoint);
                        double wC = edgeFunction(projectedB, projectedA, projectedPoint);

                        boolean insideTriangle = wA >= 0 && wB >= 0 && wC >= 0;
                        if (!insideTriangle)
                            continue;


                        Material voxelMaterial = Material.WHITE_CONCRETE;

                        if (modelMaterial != null) {
                            final double area = edgeFunction(projectedC, projectedB, projectedA);
                            wA /= area;
                            wB /= area;
                            wC /= area;

                            Vector3 voxelUV = new Vector3(
                                    wA * aUV.x + wB * bUV.x + wC * cUV.x,
                                    wA * aUV.y + wB * bUV.y + wC * cUV.y,
                                    0
                            );

                            voxelMaterial = VoxelMaterial.getMatchingMaterial(
                                    modelMaterial.getColorAt(voxelUV),
                                    modelMaterial.isTransparentAt(voxelUV)
                            );
                        }

                        voxels.add(new Voxel(new Vector3(x, y, z), voxelMaterial));
                    }
                }
            }
        }

        return voxels;
    }
}
