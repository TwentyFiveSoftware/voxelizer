package dev.twentyfive.voxelizer.util;

import dev.twentyfive.voxelizer.Voxelizer;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PathHelper {

    private static final Pattern REGEX_ALLOWED_OBJ_PATH = Pattern.compile("^[a-zA-Z0-9/_-]*(\\.obj)?$");

    public static File getFile(String filename) {
        createDataFolder();
        return new File(FileSystems.getDefault().getPath(Voxelizer.instance.getDataFolder().getAbsolutePath(), filename).toString());
    }

    public static List<String> getFilesInPath(String path) {
        try {
            return Files.walk(getFile("").toPath())
                    .map(p -> toRelativePath(p.toAbsolutePath().toString()))
                    .filter(p -> p.startsWith(path))
                    .filter(PathHelper::isAllowedObjPath)
                    .filter(p -> !p.startsWith("/"))
                    .toList();

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void createDataFolder() {
        if (!Voxelizer.instance.getDataFolder().exists())
            Voxelizer.instance.getDataFolder().mkdir();
    }

    public static boolean isAllowedObjPath(String path) {
        return REGEX_ALLOWED_OBJ_PATH.matcher(path.trim()).matches();
    }

    public static boolean doesObjFileExist(String path) {
        if (!isAllowedObjPath(path))
            return false;

        return getFile(path).exists();
    }

    private static String toRelativePath(String absolutePath) {
        return absolutePath.replace(Voxelizer.instance.getDataFolder().getAbsolutePath() + FileSystems.getDefault().getSeparator(), "");
    }

}
