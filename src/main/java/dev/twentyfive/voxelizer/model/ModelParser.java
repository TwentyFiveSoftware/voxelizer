package dev.twentyfive.voxelizer.model;

import dev.twentyfive.voxelizer.math.Triangle;
import dev.twentyfive.voxelizer.math.Vector3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ModelParser {

    private static String getPathToFile(String filename) {
        return Paths.get("").toAbsolutePath()
                + FileSystems.getDefault().getSeparator()
                + "plugins"
                + FileSystems.getDefault().getSeparator()
                + "Voxelizer"
                + FileSystems.getDefault().getSeparator()
                + filename;
    }

    public static Model parseModelFromFile(String filename, double scale) {
        ArrayList<Vector3> vertices = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(getPathToFile(filename)))) {
            for (String line; (line = br.readLine()) != null; ) {
                if (line.startsWith("v ")) {
                    String[] xyz = line.substring(2).trim().split(" ");
                    Vector3 vertex = new Vector3(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]), Double.parseDouble(xyz[2]));
                    vertices.add(Vector3.multiply(vertex, scale));

                } else if (line.startsWith("f ")) {
                    String[] abc = line.substring(2).trim().split(" ");
                    int a = Integer.parseInt(abc[0].split("/")[0]) - 1;
                    int b = Integer.parseInt(abc[1].split("/")[0]) - 1;
                    int c = Integer.parseInt(abc[2].split("/")[0]) - 1;
                    triangles.add(new Triangle(a, b, c));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Model(vertices.toArray(Vector3[]::new), triangles.toArray(Triangle[]::new));
    }

}
