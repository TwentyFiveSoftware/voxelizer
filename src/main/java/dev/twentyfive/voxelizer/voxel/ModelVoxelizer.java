package dev.twentyfive.voxelizer.voxel;

import dev.twentyfive.voxelizer.math.Triangle;
import dev.twentyfive.voxelizer.model.Model;
import dev.twentyfive.voxelizer.util.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModelVoxelizer {

    public static Voxel[] voxelizeModel(Model model, double thickness) {
        final int numberOfThreads = Runtime.getRuntime().availableProcessors();
        int trianglesPerThread = model.triangles.length / numberOfThreads;
        Thread[] threads = new Thread[numberOfThreads];

        List<Voxel> voxels = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < numberOfThreads; i++) {
            Triangle[] triangles = Arrays.copyOfRange(model.triangles, i * trianglesPerThread, Math.min((i + 1) * trianglesPerThread, model.triangles.length));
            threads[i] = new Thread(new VoxelizerThread(model, triangles, voxels, thickness));
            threads[i].start();
        }

        try {
            for (Thread thread : threads)
                thread.join();

        } catch (InterruptedException e) {
            Logger.log("Voxelizing thread interrupted!");
        }

        return voxels.toArray(Voxel[]::new);
    }

}
