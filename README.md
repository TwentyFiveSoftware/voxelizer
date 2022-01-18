# Voxelizer

**Voxelizer** is a Minecraft server plugin which builds 3D model representations in Minecraft.

To achieve this, it executes following steps:
1. Load and parse a .obj model file to get the vertices and triangles of the model
2. "Voxelize" every triangle (the heart of the plugin)
3. Spawn build tasks to build voxels in the Minecraft world
