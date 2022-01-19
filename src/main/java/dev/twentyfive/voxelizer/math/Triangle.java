package dev.twentyfive.voxelizer.math;

import dev.twentyfive.voxelizer.model.Material;

public class Triangle {
    public int a, b, c;
    public Material material;

    public Triangle(int a, int b, int c, Material material) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.material = material;
    }

    @Override
    public String toString() {
        return "Triangle (" + a + ", " + b + ", " + c + ")";
    }
}
