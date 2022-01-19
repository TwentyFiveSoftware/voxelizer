package dev.twentyfive.voxelizer.model;

import dev.twentyfive.voxelizer.math.Vector3;
import dev.twentyfive.voxelizer.util.ColorHelper;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class VoxelMaterial {

    private static final ArrayList<VoxelMaterial> VOXEL_MATERIALS = new ArrayList<>(List.of(
            new VoxelMaterial(Material.PURPLE_WOOL, new Vector3(121, 42, 172)),
            new VoxelMaterial(Material.PURPLE_TERRACOTTA, new Vector3(118, 70, 86)),
            new VoxelMaterial(Material.YELLOW_WOOL, new Vector3(248, 197, 39)),
            new VoxelMaterial(Material.LIME_WOOL, new Vector3(112, 185, 25)),
            new VoxelMaterial(Material.RED_CONCRETE, new Vector3(142, 32, 32)),
            new VoxelMaterial(Material.MAGENTA_TERRACOTTA, new Vector3(149, 88, 108)),
            new VoxelMaterial(Material.GREEN_WOOL, new Vector3(84, 109, 27)),
            new VoxelMaterial(Material.GREEN_TERRACOTTA, new Vector3(76, 83, 42)),
            new VoxelMaterial(Material.WHITE_WOOL, new Vector3(233, 236, 236)),
            new VoxelMaterial(Material.LIME_TERRACOTTA, new Vector3(103, 117, 52)),
            new VoxelMaterial(Material.CYAN_WOOL, new Vector3(21, 137, 145)),
            new VoxelMaterial(Material.GRAY_CONCRETE, new Vector3(54, 57, 61)),
            new VoxelMaterial(Material.LIGHT_BLUE_CONCRETE, new Vector3(35, 137, 198)),
            new VoxelMaterial(Material.PINK_WOOL, new Vector3(237, 141, 172)),
            new VoxelMaterial(Material.LIGHT_GRAY_CONCRETE, new Vector3(125, 125, 115)),
            new VoxelMaterial(Material.ORANGE_CONCRETE, new Vector3(224, 97, 0)),
            new VoxelMaterial(Material.BLUE_WOOL, new Vector3(53, 57, 157)),
            new VoxelMaterial(Material.LIGHT_GRAY_TERRACOTTA, new Vector3(135, 106, 97)),
            new VoxelMaterial(Material.BLUE_TERRACOTTA, new Vector3(74, 59, 91)),
            new VoxelMaterial(Material.ORANGE_TERRACOTTA, new Vector3(161, 83, 37)),
            new VoxelMaterial(Material.BLACK_TERRACOTTA, new Vector3(37, 22, 16)),
            new VoxelMaterial(Material.PURPLE_CONCRETE, new Vector3(100, 31, 156)),
            new VoxelMaterial(Material.GRAY_WOOL, new Vector3(62, 68, 71)),
            new VoxelMaterial(Material.WHITE_TERRACOTTA, new Vector3(209, 178, 161)),
            new VoxelMaterial(Material.BLACK_WOOL, new Vector3(20, 21, 25)),
            new VoxelMaterial(Material.LIGHT_BLUE_TERRACOTTA, new Vector3(113, 108, 137)),
            new VoxelMaterial(Material.BROWN_CONCRETE, new Vector3(96, 59, 31)),
            new VoxelMaterial(Material.LIGHT_GRAY_WOOL, new Vector3(142, 142, 134)),
            new VoxelMaterial(Material.BROWN_TERRACOTTA, new Vector3(77, 51, 35)),
            new VoxelMaterial(Material.CYAN_TERRACOTTA, new Vector3(86, 91, 91)),
            new VoxelMaterial(Material.MAGENTA_WOOL, new Vector3(189, 68, 179)),
            new VoxelMaterial(Material.BLACK_CONCRETE, new Vector3(8, 10, 15)),
            new VoxelMaterial(Material.CYAN_CONCRETE, new Vector3(21, 119, 136)),
            new VoxelMaterial(Material.YELLOW_CONCRETE, new Vector3(240, 175, 21)),
            new VoxelMaterial(Material.LIGHT_BLUE_WOOL, new Vector3(58, 175, 217)),
            new VoxelMaterial(Material.WHITE_CONCRETE, new Vector3(207, 213, 214)),
            new VoxelMaterial(Material.RED_TERRACOTTA, new Vector3(143, 61, 46)),
            new VoxelMaterial(Material.YELLOW_TERRACOTTA, new Vector3(186, 133, 35)),
            new VoxelMaterial(Material.ORANGE_WOOL, new Vector3(240, 118, 19)),
            new VoxelMaterial(Material.GREEN_CONCRETE, new Vector3(73, 91, 36)),
            new VoxelMaterial(Material.BROWN_WOOL, new Vector3(114, 71, 40)),
            new VoxelMaterial(Material.RED_WOOL, new Vector3(160, 39, 34)),
            new VoxelMaterial(Material.PINK_CONCRETE, new Vector3(213, 101, 142)),
            new VoxelMaterial(Material.PINK_TERRACOTTA, new Vector3(161, 78, 78)),
            new VoxelMaterial(Material.BLUE_CONCRETE, new Vector3(44, 46, 143)),
            new VoxelMaterial(Material.MAGENTA_CONCRETE, new Vector3(169, 48, 159)),
            new VoxelMaterial(Material.LIME_CONCRETE, new Vector3(94, 168, 24)),
            new VoxelMaterial(Material.GRAY_TERRACOTTA, new Vector3(57, 42, 35))
    ));

    private static final ArrayList<VoxelMaterial> TRANSPARENT_VOXEL_MATERIALS = new ArrayList<>(List.of(
            new VoxelMaterial(Material.LIGHT_BLUE_STAINED_GLASS, new Vector3(102, 153, 216)),
            new VoxelMaterial(Material.CYAN_STAINED_GLASS, new Vector3(76, 127, 153)),
            new VoxelMaterial(Material.MAGENTA_STAINED_GLASS, new Vector3(178, 76, 216)),
            new VoxelMaterial(Material.BROWN_STAINED_GLASS, new Vector3(102, 76, 51)),
            new VoxelMaterial(Material.BLACK_STAINED_GLASS, new Vector3(25, 25, 25)),
            new VoxelMaterial(Material.WHITE_STAINED_GLASS, new Vector3(255, 255, 255)),
            new VoxelMaterial(Material.LIGHT_GRAY_STAINED_GLASS, new Vector3(153, 153, 153)),
            new VoxelMaterial(Material.YELLOW_STAINED_GLASS, new Vector3(229, 229, 51)),
            new VoxelMaterial(Material.PURPLE_STAINED_GLASS, new Vector3(127, 63, 178)),
            new VoxelMaterial(Material.BLUE_STAINED_GLASS, new Vector3(51, 76, 178)),
            new VoxelMaterial(Material.ORANGE_STAINED_GLASS, new Vector3(216, 127, 51)),
            new VoxelMaterial(Material.GRAY_STAINED_GLASS, new Vector3(76, 76, 76)),
            new VoxelMaterial(Material.TINTED_GLASS, new Vector3(43, 38, 45)),
            new VoxelMaterial(Material.GREEN_STAINED_GLASS, new Vector3(102, 127, 51)),
            new VoxelMaterial(Material.RED_STAINED_GLASS, new Vector3(153, 51, 51)),
            new VoxelMaterial(Material.LIME_STAINED_GLASS, new Vector3(127, 204, 25)),
            new VoxelMaterial(Material.PINK_STAINED_GLASS, new Vector3(242, 127, 165))
    ));

    public Material material;
    public Vector3 color;

    public VoxelMaterial(Material material, Vector3 color) {
        this.material = material;
        this.color = color;
    }

    public static Material getMatchingMaterial(Vector3 color, boolean transparent) {
        Material matchingMaterial = Material.AIR;
        double smallestDifference = Double.MAX_VALUE;

        for (VoxelMaterial voxelMaterial : (transparent ? TRANSPARENT_VOXEL_MATERIALS : VOXEL_MATERIALS)) {
            final double difference = ColorHelper.calculateColorDifference(color, voxelMaterial.color);

            if (difference < smallestDifference) {
                smallestDifference = difference;
                matchingMaterial = voxelMaterial.material;
            }
        }

        return matchingMaterial;
    }

}
