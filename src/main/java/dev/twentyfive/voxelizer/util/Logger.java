package dev.twentyfive.voxelizer.util;

import org.bukkit.Bukkit;

public class Logger {

    private static final java.util.logging.Logger logger = Bukkit.getLogger();

    public static void log() {
        logger.info("");
    }

    public static void log(String message) {
        logger.info(message);
    }

}
