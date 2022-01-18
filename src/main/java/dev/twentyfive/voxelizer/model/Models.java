package dev.twentyfive.voxelizer.model;

import dev.twentyfive.voxelizer.math.Vector3;

public enum Models {
    TEST_TRIANGLE(new Model(
            new Vector3[]{new Vector3(0, 0, 0), new Vector3(14.2, 30.5, 20.3), new Vector3(5.5, 10.3, 15.9)},
            new int[]{0, 1, 2}
    ));

    public final Model model;

    Models(Model model) {
        this.model = model;
    }
}
