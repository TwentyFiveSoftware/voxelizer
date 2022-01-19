package dev.twentyfive.voxelizer.model;

import dev.twentyfive.voxelizer.math.Triangle;
import dev.twentyfive.voxelizer.math.Vector3;

public class Model {
    public final Vector3[] vertices;
    public final Triangle[] triangles;
    public final Vector3[] uvs;

    public Model(Vector3[] vertices, Triangle[] triangles, Vector3[] uvs) {
        this.vertices = vertices;
        this.triangles = triangles;
        this.uvs = uvs;
    }

}
