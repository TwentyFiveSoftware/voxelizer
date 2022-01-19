package dev.twentyfive.voxelizer.model;

import dev.twentyfive.voxelizer.math.Triangle;
import dev.twentyfive.voxelizer.math.Vector3;
import dev.twentyfive.voxelizer.util.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelParser {

    private static final Pattern REGEX_VERTEX = Pattern.compile("^v ([0-9-.]+) ([0-9-.]+) ([0-9-.]+)$");
    private static final Pattern REGEX_UV = Pattern.compile("^vt ([0-9-.]+) ([0-9-.]+)$");
    private static final Pattern REGEX_TRIANGLE =
            Pattern.compile("^f ([0-9]+)/?([0-9]*)/?[0-9]* ([0-9]+)/?([0-9]*)/?[0-9]* ([0-9]+)/?([0-9]*)/?[0-9]*(?: ([0-9]+)/?([0-9]*)/?[0-9]*)?$");

    public static Model parseModelFromFile(String filename, double scale) {
        ArrayList<Vector3> vertices = new ArrayList<>();
        ArrayList<Vector3> uvs = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();

        Map<String, ModelMaterial> materials = new HashMap<>();
        String materialsFolderPath = new File(getPathToFile(filename)).getParent();

        try (BufferedReader br = new BufferedReader(new FileReader(getPathToFile(filename)))) {
            ModelMaterial triangleMaterial = null;

            for (String line; (line = br.readLine()) != null; ) {
                if (line.startsWith("v "))
                    vertices.add(parseLineVertex(line, scale));

                else if (line.startsWith("vt "))
                    uvs.add(parseLineUV(line));

                else if (line.startsWith("f "))
                    triangles.addAll(parseLineTriangle(line, triangleMaterial));

                else if (line.startsWith("usemtl "))
                    triangleMaterial = materials.getOrDefault(line.substring(7), null);

                else if (line.startsWith("mtllib "))
                    materials.putAll(MaterialLibParser.parseMaterialLib(materialsFolderPath, line.substring(7).trim()));
            }

        } catch (IOException e) {
            Logger.log("Model file not found: " + filename);
        }

        return new Model(vertices.toArray(Vector3[]::new), triangles.toArray(Triangle[]::new), uvs.toArray(Vector3[]::new));
    }

    private static String getPathToFile(String filename) {
        return Paths.get("").toAbsolutePath()
                + FileSystems.getDefault().getSeparator()
                + "plugins"
                + FileSystems.getDefault().getSeparator()
                + "Voxelizer"
                + FileSystems.getDefault().getSeparator()
                + filename;
    }

    private static Vector3 parseLineVertex(String line, double scale) {
        Matcher matcher = REGEX_VERTEX.matcher(line.trim());
        if (!matcher.find())
            return Vector3.zero();

        Vector3 vertex = new Vector3(
                Double.parseDouble(matcher.group(1)),
                Double.parseDouble(matcher.group(2)),
                Double.parseDouble(matcher.group(3))
        );

        return Vector3.multiply(vertex, scale);
    }

    private static Vector3 parseLineUV(String line) {
        Matcher matcher = REGEX_UV.matcher(line.trim());
        if (!matcher.find())
            return Vector3.zero();

        return new Vector3(
                Double.parseDouble(matcher.group(1)),
                Double.parseDouble(matcher.group(2)),
                0
        );
    }

    private static ArrayList<Triangle> parseLineTriangle(String line, ModelMaterial triangleMaterial) {
        ArrayList<Triangle> triangles = new ArrayList<>();

        Matcher matcher = REGEX_TRIANGLE.matcher(line.trim());
        if (!matcher.find())
            return triangles;

        int a = Integer.parseInt(matcher.group(1)) - 1;
        int b = Integer.parseInt(matcher.group(3)) - 1;
        int c = Integer.parseInt(matcher.group(5)) - 1;

        int aUV = (matcher.group(2).isEmpty() ? 0 : Integer.parseInt(matcher.group(2))) - 1;
        int bUV = (matcher.group(4).isEmpty() ? 0 : Integer.parseInt(matcher.group(4))) - 1;
        int cUV = (matcher.group(6).isEmpty() ? 0 : Integer.parseInt(matcher.group(6))) - 1;

        if (matcher.group(7) != null) {
            int d = Integer.parseInt(matcher.group(7)) - 1;
            int dUV = (matcher.group(8).isEmpty() ? 0 : Integer.parseInt(matcher.group(8))) - 1;
            triangles.add(new Triangle(a, c, d, aUV, cUV, dUV, triangleMaterial));
        }

        triangles.add(new Triangle(a, b, c, aUV, bUV, cUV, triangleMaterial));
        return triangles;
    }

}
