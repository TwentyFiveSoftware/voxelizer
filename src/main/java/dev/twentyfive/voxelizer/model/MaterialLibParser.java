package dev.twentyfive.voxelizer.model;

import dev.twentyfive.voxelizer.math.Vector3;
import dev.twentyfive.voxelizer.util.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaterialLibParser {

    private static final Pattern REGEX_DIFFUSE_COLOR = Pattern.compile("^Kd ([0-9.]+) ([0-9.]+) ([0-9.]+)$");

    public static Map<String, ModelMaterial> parseMaterialLib(String folderPath, String filename) {
        Map<String, ModelMaterial> materials = new HashMap<>();

        String materialLibPath = folderPath + FileSystems.getDefault().getSeparator() + filename;

        try (BufferedReader br = new BufferedReader(new FileReader(materialLibPath))) {
            String currentMaterialName = "";

            for (String line; (line = br.readLine()) != null; ) {
                if (line.startsWith("newmtl ")) {
                    currentMaterialName = line.substring(7);
                    materials.put(currentMaterialName, new ModelMaterial(null, Vector3.one(), false));

                } else if (line.startsWith("Kd "))
                    materials.get(currentMaterialName).diffuseColor = parseLineDiffuseColor(line);

                else if (line.startsWith("d "))
                    materials.get(currentMaterialName).completelyTransparent = parseLineOpacity(line) != 1.0;

                else if (line.startsWith("map_"))
                    materials.get(currentMaterialName).texture = parseLineMapTexture(line, folderPath);
            }

        } catch (IOException e) {
            Logger.log("Material library file not found: " + materialLibPath);
        }

        return materials;
    }

    private static Vector3 parseLineDiffuseColor(String line) {
        Matcher matcher = REGEX_DIFFUSE_COLOR.matcher(line.trim());
        if (!matcher.find())
            return Vector3.one();

        return new Vector3(
                Double.parseDouble(matcher.group(1)),
                Double.parseDouble(matcher.group(2)),
                Double.parseDouble(matcher.group(3))
        );
    }

    private static double parseLineOpacity(String line) {
        return Double.parseDouble(line.substring(2).trim());
    }

    private static BufferedImage parseLineMapTexture(String line, String folderPath) {
        String[] split = line.split(" ");
        String textureFilename = split[split.length - 1];
        String texturePath = folderPath + FileSystems.getDefault().getSeparator() + textureFilename;

        BufferedImage texture = null;
        try {
            texture = ImageIO.read(new File(texturePath));
        } catch (IOException e) {
            Logger.log("Texture file not found: " + texturePath);
        }

        return texture;
    }

}
