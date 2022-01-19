package dev.twentyfive.voxelizer.math;

import dev.twentyfive.voxelizer.model.Model;
import dev.twentyfive.voxelizer.util.ColorHelper;
import dev.twentyfive.voxelizer.util.Voxel;
import org.bukkit.Material;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private static final Map<Material, Vector3> MATERIAL_COLORS = new HashMap<>() {{
        put(Material.BLUE_CONCRETE, new Vector3(44, 46, 143));
        put(Material.BLACK_CONCRETE, new Vector3(8, 10, 15));
        put(Material.RED_CONCRETE, new Vector3(142, 32, 32));
        put(Material.GREEN_CONCRETE, new Vector3(73, 91, 36));
        put(Material.BROWN_CONCRETE, new Vector3(96, 59, 31));
        put(Material.PURPLE_CONCRETE, new Vector3(100, 31, 156));
        put(Material.CYAN_CONCRETE, new Vector3(21, 119, 136));
        put(Material.LIGHT_GRAY_CONCRETE, new Vector3(125, 125, 115));
        put(Material.GRAY_CONCRETE, new Vector3(54, 57, 61));
        put(Material.PINK_CONCRETE, new Vector3(213, 101, 142));
        put(Material.LIME_CONCRETE, new Vector3(94, 168, 24));
        put(Material.YELLOW_CONCRETE, new Vector3(240, 175, 21));
        put(Material.LIGHT_BLUE_CONCRETE, new Vector3(35, 137, 198));
        put(Material.MAGENTA_CONCRETE, new Vector3(169, 48, 159));
        put(Material.ORANGE_CONCRETE, new Vector3(224, 97, 0));
        put(Material.WHITE_CONCRETE, new Vector3(207, 213, 214));
    }};

    public static Material getMatchingMaterial(Vector3 color) {
        Material matchingMaterial = Material.NETHER_BRICKS;
        double smallestDifference = Double.MAX_VALUE;

        for (Map.Entry<Material, Vector3> material : MATERIAL_COLORS.entrySet()) {
            double difference = ColorHelper.calculateColorDifference(color, material.getValue());

            if (difference < smallestDifference) {
                smallestDifference = difference;
                matchingMaterial = material.getKey();
            }
        }

        return matchingMaterial;
    }

    private static Vector3 getColorAt(BufferedImage texture, Vector3 uv) {
        int rgb = texture.getRGB((int) (texture.getWidth() * uv.x), (int) (texture.getHeight() * (1 - uv.y)));
        return new Vector3((rgb & 0xFF0000) >> 16, (rgb & 0xFF00) >> 8, rgb & 0xFF);
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

            BufferedImage texture = triangle.material == null ? null : triangle.material.texture;


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

                        double area = edgeFunction(projectedC, projectedB, projectedA);
                        wA /= area;
                        wB /= area;
                        wC /= area;

                        Vector3 voxelUV = new Vector3(
                                wA * aUV.x + wB * bUV.x + wC * cUV.x,
                                wA * aUV.y + wB * bUV.y + wC * cUV.y,
                                0
                        );

                        Vector3 voxelColor = texture == null ? new Vector3(0xFF, 0xFF, 0xFF) : getColorAt(texture, voxelUV);
                        Material voxelMaterial = getMatchingMaterial(voxelColor);
                        voxels.add(new Voxel(new Vector3(x, y, z), voxelMaterial));
                    }
                }
            }
        }

        return voxels;
    }
}
