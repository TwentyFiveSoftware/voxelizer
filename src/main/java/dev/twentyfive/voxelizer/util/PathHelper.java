package dev.twentyfive.voxelizer.util;

import dev.twentyfive.voxelizer.Voxelizer;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.regex.Pattern;

public class PathHelper {

    private static final Pattern REGEX_ALLOWED_OBJ_PATH = Pattern.compile("^[a-zA-Z0-9/_-]+\\.obj$");

    public static File getFile(String filename) {
        createDataFolder();
        return new File(FileSystems.getDefault().getPath(Voxelizer.instance.getDataFolder().getAbsolutePath(), filename).toString());
    }

    public static boolean isAllowedObjPath(String path) {
        return REGEX_ALLOWED_OBJ_PATH.matcher(path.trim()).matches();
    }

    public static boolean doesObjFileExist(String path) {
        if (!isAllowedObjPath(path))
            return false;

        return getFile(path).exists();
    }

    private static void createDataFolder() {
        if (!Voxelizer.instance.getDataFolder().exists()) Voxelizer.instance.getDataFolder().mkdir();
    }

}
