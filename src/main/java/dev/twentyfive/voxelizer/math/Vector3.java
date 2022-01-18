package dev.twentyfive.voxelizer.math;

public class Vector3 {
    public double x, y, z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3 crossProduct(Vector3 a, Vector3 b) {
        return new Vector3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }

    public static double dotProduct(Vector3 a, Vector3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector3 normalize() {
        double magnitude = this.magnitude();
        this.x /= magnitude;
        this.y /= magnitude;
        this.z /= magnitude;
        return this;
    }

    public static Vector3 add(Vector3 a, Vector3 b) {
        return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vector3 sub(Vector3 a, Vector3 b) {
        return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vector3 multiply(Vector3 v, double scalar) {
        return new Vector3(v.x * scalar, v.y * scalar, v.z * scalar);
    }

    @Override
    public String toString() {
        return "Vector3 (" + x + ", " + y + ", " + z + ")";
    }
}
