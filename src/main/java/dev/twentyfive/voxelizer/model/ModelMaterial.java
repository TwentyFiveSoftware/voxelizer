package dev.twentyfive.voxelizer.model;

import dev.twentyfive.voxelizer.math.Vector3;

import java.awt.image.BufferedImage;

public class ModelMaterial {

    public final BufferedImage texture;

    public ModelMaterial(BufferedImage texture) {
        this.texture = texture;
    }

    public Vector3 getColorAt(Vector3 uv) {
        final int argb = getARGBAt(uv);
        return new Vector3((argb & 0xFF0000) >>> 16, (argb & 0xFF00) >>> 8, argb & 0xFF);
    }

    public boolean isTransparentAt(Vector3 uv) {
        final int argb = getARGBAt(uv);
        return (argb & 0xFF000000) >>> 24 != 0xFF;
    }

    private int getARGBAt(Vector3 uv) {
        int x = (int) (this.texture.getWidth() * uv.x);
        int y = (int) (this.texture.getHeight() * (1 - uv.y));
        return this.texture.getRGB(x, y);
    }

}
