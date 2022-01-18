package dev.twentyfive.voxelizer.model;

import dev.twentyfive.voxelizer.math.Triangle;
import dev.twentyfive.voxelizer.math.Vector3;

public enum Models {
    TEST_TRIANGLE(new Model(
            new Vector3[]{new Vector3(2, 1.5, 0.2), new Vector3(20.2, 30.5, 20.3), new Vector3(25.5, 5.3, 15.9)},
            new Triangle[]{new Triangle(0, 1, 2)}
    ));

    public final Model model;

    Models(Model model) {
        this.model = model;
    }
}
