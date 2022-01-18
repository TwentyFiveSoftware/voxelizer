package dev.twentyfive.voxelizer.model;

import dev.twentyfive.voxelizer.math.Vector3;

public class Model {
    public final Vector3[] vertices;
    public final int[] triangles;

    public Model(Vector3[] vertices, int[] triangles) {
        this.vertices = vertices;
        this.triangles = triangles;
    }

}
