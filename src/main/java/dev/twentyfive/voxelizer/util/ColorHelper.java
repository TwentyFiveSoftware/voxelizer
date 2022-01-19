package dev.twentyfive.voxelizer.util;

import dev.twentyfive.voxelizer.math.Vector3;

public class ColorHelper {

    public static double calculateColorDifference(Vector3 a, Vector3 b) {
        return calculateCIEDeltaEColorDifference(
                convertXYZtoCIELab(convertRGBtoXYZ(a)),
                convertXYZtoCIELab(convertRGBtoXYZ(b))
        );
    }

    private static double calculateCIEDeltaEColorDifference(Vector3 c1, Vector3 c2) {
        return Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2) + Math.pow(c1.z - c2.z, 2));
    }

    private static Vector3 convertRGBtoXYZ(Vector3 c) {
        c = Vector3.multiply(c, 1.0 / 0xFF);

        c.x = c.x >= 0.04045 ? Math.pow((c.x + 0.055) / 1.055, 2.4) : c.x / 12.92;
        c.y = c.y >= 0.04045 ? Math.pow((c.y + 0.055) / 1.055, 2.4) : c.y / 12.92;
        c.z = c.z >= 0.04045 ? Math.pow((c.z + 0.055) / 1.055, 2.4) : c.z / 12.92;

        c = Vector3.multiply(c, 100);

        return new Vector3(
                c.x * 0.4124564 + c.y * 0.3575761 + c.z * 0.1804375,
                c.x * 0.2126729 + c.y * 0.7151522 + c.z * 0.0721750,
                c.x * 0.0192229 + c.y * 0.1191920 + c.z * 0.9503041
        );
    }

    private static Vector3 convertXYZtoCIELab(Vector3 c) {
        c.x /= 95.047;
        c.y /= 100.0;
        c.z /= 108.883;

        c.x = c.x > 0.008856 ? Math.pow(c.x, 1.0 / 3.0) : (7.787 * c.x + 16.0 / 116.0);
        c.y = c.y > 0.008856 ? Math.pow(c.y, 1.0 / 3.0) : (7.787 * c.y + 16.0 / 116.0);
        c.z = c.z > 0.008856 ? Math.pow(c.z, 1.0 / 3.0) : (7.787 * c.z + 16.0 / 116.0);

        return new Vector3(
                116.0 * c.y - 16.0,
                500.0 * (c.x - c.y),
                200.0 * (c.y - c.z)
        );
    }

}
