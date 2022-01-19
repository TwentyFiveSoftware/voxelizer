package dev.twentyfive.voxelizer.math;

import dev.twentyfive.voxelizer.model.Material;

public class Triangle {
    public int a, b, c;
    public int aUV, bUV, cUV;
    public Material material;

    public Triangle(int a, int b, int c, int aUV, int bUV, int cUV, Material material) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.aUV = aUV;
        this.bUV = bUV;
        this.cUV = cUV;
        this.material = material;
    }

    @Override
    public String toString() {
        return "Triangle (" + a + ", " + b + ", " + c + ")";
    }
}
