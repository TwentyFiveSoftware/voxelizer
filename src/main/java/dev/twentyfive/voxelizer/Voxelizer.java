package dev.twentyfive.voxelizer;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Voxelizer {
    static class Vector {
        double x, y;

        public Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Triangle {
        int a, b, c;

        public Triangle(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    static final Vector[] vertices = {new Vector(3.5, 8.1), new Vector(25.3, 27.8), new Vector(17.9, 2.5)};
    static final Triangle[] triangles = {new Triangle(0, 1, 2)};

    static boolean edgeFunction(Vector a, Vector b, Vector point) {
        return (point.x - a.x) * (b.y - a.y) - (point.y - a.y) * (b.x - a.x) >= 0;
    }

    public static void main(String[] args) {
        final int IMAGE_SIZE = 30;
        int[] pixels = new int[3 * IMAGE_SIZE * IMAGE_SIZE];
        Arrays.fill(pixels, 0xFF);


        for (Triangle triangle : triangles) {
            Vector a = vertices[triangle.a];
            Vector b = vertices[triangle.b];
            Vector c = vertices[triangle.c];

            int minX = (int) Math.min(a.x, Math.min(b.x, c.x));
            int minY = (int) Math.min(a.y, Math.min(b.y, c.y));
            int maxX = (int) Math.max(a.x, Math.max(b.x, c.x));
            int maxY = (int) Math.max(a.y, Math.max(b.y, c.y));

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    Vector point = new Vector(x, y);
                    boolean inside = edgeFunction(a, b, point) && edgeFunction(b, c, point) && edgeFunction(c, a, point);

                    if (inside) {
                        int index = 3 * ((IMAGE_SIZE - y - 1) * IMAGE_SIZE + x);
                        pixels[index] = 0xFF;
                        pixels[index + 1] = 0x00;
                        pixels[index + 2] = 0x00;
                    }
                }
            }
        }

        saveImage(pixels, IMAGE_SIZE, IMAGE_SIZE);
    }

    public static void saveImage(int[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();
        raster.setPixels(0, 0, width, height, pixels);

        try {
            ImageIO.write(image, "png", new File("output.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
