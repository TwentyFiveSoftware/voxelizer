package dev.twentyfive.voxelizer.voxel;

import dev.twentyfive.voxelizer.math.Geometry;
import dev.twentyfive.voxelizer.math.Triangle;
import dev.twentyfive.voxelizer.math.Vector3;
import dev.twentyfive.voxelizer.model.Model;
import dev.twentyfive.voxelizer.model.ModelMaterial;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class VoxelizerThread implements Runnable {
    private final Model model;
    private final Triangle[] triangles;
    private final List<Voxel> outputVoxelList;
    private final double thickness;

    public VoxelizerThread(Model model, Triangle[] triangles, List<Voxel> outputVoxelList, double thickness) {
        this.model = model;
        this.triangles = triangles;
        this.outputVoxelList = outputVoxelList;
        this.thickness = thickness;
    }

    @Override
    public void run() {
        ArrayList<Voxel> voxels = new ArrayList<>();

        for (Triangle triangle : this.triangles) {
            Vector3 a = model.vertices[triangle.a];
            Vector3 b = model.vertices[triangle.b];
            Vector3 c = model.vertices[triangle.c];


            Vector3 planeNormal = Geometry.calculateNormalizedPlaneNormal(a, b, c);
            Vector3 projectedA = Geometry.projectVector3on2DPlaneCoordinates(planeNormal, a, b, a);
            Vector3 projectedB = Geometry.projectVector3on2DPlaneCoordinates(planeNormal, a, b, b);
            Vector3 projectedC = Geometry.projectVector3on2DPlaneCoordinates(planeNormal, a, b, c);

            Vector3 aUV = (triangle.aUV >= 0 && model.uvs.length > triangle.aUV) ? model.uvs[triangle.aUV] : Vector3.zero();
            Vector3 bUV = (triangle.bUV >= 0 && model.uvs.length > triangle.bUV) ? model.uvs[triangle.bUV] : Vector3.zero();
            Vector3 cUV = (triangle.cUV >= 0 && model.uvs.length > triangle.cUV) ? model.uvs[triangle.cUV] : Vector3.zero();

            ModelMaterial modelMaterial = triangle.material;


            int minX = (int) (Math.min(a.x, Math.min(b.x, c.x)) - Math.ceil(thickness));
            int minY = (int) (Math.min(a.y, Math.min(b.y, c.y)) - Math.ceil(thickness));
            int minZ = (int) (Math.min(a.z, Math.min(b.z, c.z)) - Math.ceil(thickness));

            int maxX = (int) (Math.max(a.x, Math.max(b.x, c.x)) + Math.ceil(thickness));
            int maxY = (int) (Math.max(a.y, Math.max(b.y, c.y)) + Math.ceil(thickness));
            int maxZ = (int) (Math.max(a.z, Math.max(b.z, c.z)) + Math.ceil(thickness));

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Vector3 point = new Vector3(x + 0.5, y + 0.5, z + 0.5);

                        double distance = Math.abs(Geometry.calculateSignedDistanceBetweenPlaneAndPoint(planeNormal, a, point));
                        if (distance > thickness)
                            continue;

                        Vector3 projectedPoint = Geometry.projectVector3on2DPlaneCoordinates(planeNormal, a, b,
                                Geometry.projectPointOnPlane(planeNormal, a, point));
                        double wA = Geometry.edgeFunction(projectedC, projectedB, projectedPoint);
                        double wB = Geometry.edgeFunction(projectedA, projectedC, projectedPoint);
                        double wC = Geometry.edgeFunction(projectedB, projectedA, projectedPoint);

                        boolean insideTriangle = wA >= 0 && wB >= 0 && wC >= 0;
                        if (!insideTriangle)
                            continue;


                        Material voxelMaterial = Material.WHITE_CONCRETE;

                        if (modelMaterial != null) {
                            double area = Geometry.edgeFunction(projectedC, projectedB, projectedA);
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

        outputVoxelList.addAll(voxels);
    }
}