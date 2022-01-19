package dev.twentyfive.voxelizer.model;

import dev.twentyfive.voxelizer.math.Vector3;

import java.awt.image.BufferedImage;

public class ModelMaterial {

    public final BufferedImage texture;
    public final Vector3 diffuseColor;
    public final boolean completelyTransparent;

    public ModelMaterial(BufferedImage texture, Vector3 diffuseColorMultiplier, boolean completelyTransparent) {
        this.texture = texture;
        this.diffuseColor = diffuseColorMultiplier;
        this.completelyTransparent = completelyTransparent;
    }

    public Vector3 getColorAt(Vector3 uv) {
        final int argb = getARGBAt(uv);
        return new Vector3(
                ((argb & 0xFF0000) >>> 16) * this.diffuseColor.x,
                ((argb & 0xFF00) >>> 8) * this.diffuseColor.y,
                (argb & 0xFF) * this.diffuseColor.z
        );
    }

    public boolean isTransparentAt(Vector3 uv) {
        if (completelyTransparent)
            return true;

        final int argb = getARGBAt(uv);
        return (argb & 0xFF000000) >>> 24 != 0xFF;
    }

    private int getARGBAt(Vector3 uv) {
        if (this.texture == null)
            return 0xFFFFFFFF;

        int x = (int) (this.texture.getWidth() * uv.x);
        int y = (int) (this.texture.getHeight() * (1 - uv.y));
        return this.texture.getRGB(x, y);
    }

}
