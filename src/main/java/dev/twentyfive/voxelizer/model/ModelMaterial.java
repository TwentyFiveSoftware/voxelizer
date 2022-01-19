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
        if (this.texture == null || this.texture.getWidth() == 0 || this.texture.getHeight() == 0)
            return 0xFFFFFFFF;

        double u = Math.min(Math.max(uv.x, 0), 1);
        double v = 1 - Math.min(Math.max(uv.y, 0), 1);

        int x = (int) (this.texture.getWidth() * u);
        int y = (int) (this.texture.getHeight() * v);

        x = Math.min(x, this.texture.getWidth() - 1);
        y = Math.min(y, this.texture.getHeight() - 1);

        return this.texture.getRGB(x, y);
    }

}
