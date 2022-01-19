package dev.twentyfive.voxelizer.model;

import dev.twentyfive.voxelizer.math.Triangle;
import dev.twentyfive.voxelizer.math.Vector3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

    private static final Pattern REGEX_TRIANGLE =
            Pattern.compile("^f ([0-9]+)/?([0-9]*)/?[0-9]* ([0-9]+)/?([0-9]*)/?[0-9]* ([0-9]+)/?([0-9]*)/?[0-9]*(?: ([0-9]+)/?([0-9]*)/?[0-9]*)?$");


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
        ArrayList<Vector3> uvs = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();

        Map<String, ModelMaterial> materials = new HashMap<>();
        String materialsFolderPath = new File(getPathToFile(filename)).getParent();

        try (BufferedReader br = new BufferedReader(new FileReader(getPathToFile(filename)))) {
            ModelMaterial currentMaterial = null;

            for (String line; (line = br.readLine()) != null; ) {
                if (line.startsWith("v ")) {
                    String[] xyz = line.substring(2).trim().split(" ");
                    Vector3 vertex = new Vector3(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]), Double.parseDouble(xyz[2]));
                    vertices.add(Vector3.multiply(vertex, scale));
                    continue;
                }

                if (line.startsWith("vt ")) {
                    String[] uv = line.substring(3).trim().split(" ");
                    uvs.add(new Vector3(Double.parseDouble(uv[0]), Double.parseDouble(uv[1]), 0));
                    continue;
                }

                if (line.startsWith("f ")) {
                    Matcher matcher = REGEX_TRIANGLE.matcher(line.trim());
                    if (!matcher.find())
                        continue;

                    int a = Integer.parseInt(matcher.group(1)) - 1;
                    int b = Integer.parseInt(matcher.group(3)) - 1;
                    int c = Integer.parseInt(matcher.group(5)) - 1;

                    int aUV = (matcher.group(2).isEmpty() ? 0 : Integer.parseInt(matcher.group(2))) - 1;
                    int bUV = (matcher.group(4).isEmpty() ? 0 : Integer.parseInt(matcher.group(4))) - 1;
                    int cUV = (matcher.group(6).isEmpty() ? 0 : Integer.parseInt(matcher.group(6))) - 1;

                    if (matcher.group(7) != null) {
                        int d = Integer.parseInt(matcher.group(7)) - 1;
                        int dUV = (matcher.group(8).isEmpty() ? 0 : Integer.parseInt(matcher.group(8))) - 1;
                        triangles.add(new Triangle(a, c, d, aUV, cUV, dUV, currentMaterial));
                    }

                    triangles.add(new Triangle(a, b, c, aUV, bUV, cUV, currentMaterial));
                    continue;
                }

                if (line.startsWith("usemtl ")) {
                    String materialName = line.substring(7).trim();
                    currentMaterial = materials.getOrDefault(materialName, null);
                    continue;
                }

                if (line.startsWith("mtllib ")) {
                    String materialLibFilename = line.substring(6).trim();
                    materials.putAll(parseMaterialLib(materialsFolderPath, materialLibFilename));
                    continue;
                }
            }
        } catch (IOException e) {
            System.out.println("Model file not found: " + filename);
        }

        return new Model(vertices.toArray(Vector3[]::new), triangles.toArray(Triangle[]::new), uvs.toArray(Vector3[]::new));
    }

    private static Map<String, ModelMaterial> parseMaterialLib(String folderPath, String filename) {
        Map<String, ModelMaterial> materials = new HashMap<>();

        String materialLibPath = folderPath + FileSystems.getDefault().getSeparator() + filename;

        try (BufferedReader br = new BufferedReader(new FileReader(materialLibPath))) {
            String currentMaterialName = "";
            Vector3 currentDiffuseColor = Vector3.one();
            boolean currentIsCompletelyTransparent = false;

            for (String line; (line = br.readLine()) != null; ) {
                if (line.startsWith("newmtl ")) {
                    if (currentMaterialName.length() > 0) {
                        materials.put(currentMaterialName, new ModelMaterial(null, currentDiffuseColor, currentIsCompletelyTransparent));
                        currentDiffuseColor = Vector3.one();
                        currentIsCompletelyTransparent = false;
                    }

                    currentMaterialName = line.substring(7).trim();
                    continue;
                }

                if (line.startsWith("Kd ")) {
                    String[] rgb = line.substring(3).trim().split(" ");
                    currentDiffuseColor = new Vector3(Double.parseDouble(rgb[0]), Double.parseDouble(rgb[1]), Double.parseDouble(rgb[2]));
                    continue;
                }

                if (line.startsWith("d ")) {
                    double opacity = Double.parseDouble(line.substring(2).trim());
                    currentIsCompletelyTransparent = opacity != 1;
                    continue;
                }

                if (line.startsWith("map_") && currentMaterialName.length() > 0) {
                    String[] split = line.split(" ");
                    String textureFilename = split[split.length - 1];
                    String texturePath = folderPath + FileSystems.getDefault().getSeparator() + textureFilename;

                    BufferedImage texture = null;
                    try {
                        texture = ImageIO.read(new File(texturePath));
                    } catch (IOException e) {
                        System.out.println("Texture file not found: " + texturePath);
                    }

                    Vector3 diffuseColor = line.startsWith("map_Kd ") ? currentDiffuseColor : Vector3.one();

                    materials.put(currentMaterialName, new ModelMaterial(texture, diffuseColor, currentIsCompletelyTransparent));
                    currentMaterialName = "";
                    currentDiffuseColor = Vector3.one();
                    currentIsCompletelyTransparent = false;
                    continue;
                }
            }

            if (!currentMaterialName.isEmpty()) {
                materials.put(currentMaterialName, new ModelMaterial(null, currentDiffuseColor, currentIsCompletelyTransparent));
            }
        } catch (IOException e) {
            System.out.println("Material library file not found: " + materialLibPath);
        }

        return materials;
    }

}
