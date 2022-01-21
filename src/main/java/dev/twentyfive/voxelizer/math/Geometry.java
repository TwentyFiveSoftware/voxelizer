package dev.twentyfive.voxelizer.math;

public class Geometry {

    public static Vector3 calculateNormalizedPlaneNormal(Vector3 a, Vector3 b, Vector3 c) {
        return Vector3.crossProduct(Vector3.sub(b, a), Vector3.sub(c, a)).normalize();
    }

    public static double calculateSignedDistanceBetweenPlaneAndPoint(Vector3 planeNormal, Vector3 planePoint, Vector3 point) {
        return Vector3.dotProduct(planeNormal, (Vector3.sub(point, planePoint)));
    }

    public static Vector3 projectPointOnPlane(Vector3 planeNormal, Vector3 planePoint, Vector3 point) {
        return Vector3.add(point, Vector3.multiply(planeNormal, -calculateSignedDistanceBetweenPlaneAndPoint(planeNormal, planePoint, point)));
    }

    public static double edgeFunction(Vector3 a, Vector3 b, Vector3 c) {
        return (c.x - a.x) * (b.y - a.y) - (c.y - a.y) * (b.x - a.x);
    }

    public static Vector3 projectVector3on2DPlaneCoordinates(Vector3 planeNormal, Vector3 planePointA, Vector3 planePointB, Vector3 point) {
        Vector3 xAxis = Vector3.sub(planePointB, planePointA).normalize();
        Vector3 yAxis = Vector3.crossProduct(planeNormal, xAxis).normalize();
        Vector3 vectorToPoint = Vector3.sub(point, planePointA);
        return new Vector3(Vector3.dotProduct(vectorToPoint, xAxis), Vector3.dotProduct(vectorToPoint, yAxis), 0);
    }

}
