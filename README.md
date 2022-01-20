![screenshot](docs/screenshots/screenshot.png)

# Voxelizer

**Voxelizer** is a Minecraft server plugin which builds 3D model representations in Minecraft.

# Overview

To achieve this, it executes following steps:

1. Load and parse a .obj model file and the corresponding .mtl file to get the vertices, triangles, uv coordinates and materials of the model
2. "Voxelize" every triangle (heart of the plugin); explained in more detail below
3. Spawn build tasks to build the voxels in the Minecraft world

![comparison](docs/screenshots/comparison.png)

## "Voxelizing"

Voxelizing is done by iterating over every triangle in the mesh. For every block in the bounding box of the triangle it is checked if that block's distance to the 3d plane of the triangle is less than the specified thickness. If this is the case, it is checked if that block lies inside the triangle. This is done by projecting the blocks 3d coordinates on a 2d coordinate system spanned by the triangle. Then a simple edge function is applied to that point to check if the point (and so the block) is inside the triangle (see rasterization). The next step is to find the closest matching Minecraft material to the triangle's material (see below).

## Material mapping

To find the closest matching material, we have to calculate the color of the triangle's material at the block's position. This is done by calculating the barycentric coordinates (the relative coordinates of the point (the projected block) in the triangle) and using them to interpolate the uv coordinates (texture coordinates) given by the vertices. Now we can look up the RGBA value at the texture at the calculated point's uv coordinates.

The next step is comparing the desired color to the colors of the Minecraft materials to find the closest matching material. The color of the Minecraft building materials are precalculated and stored in an array of possible materials. Compare two colors is pretty difficult because the sRGB color space doesn't match the human perception so in order to compare two colors respecting the human perception, we have to convert the colors to a different color space, namely the CIE-Lab color space. In this color space it's easier to calculate the difference between two colors, by using one of the delta E formulas. Now we just have to calculate the delta E value for every material and choose the one with the lowest delta E. If the color is a bit transparent, there's a second list containing just Minecraft's colored glass blocks to choose the material from.

# Using the plugin

The release section contains the packaged .jar file which you can simply drag & drop inside your PaperMC / Spigot Minecraft server's plugin directory. To use a .obj 3d model, you simply have to put it (along with it's .mtl file and texture files) somewhere inside the generated `plugins/Voxelizer` folder. It can then be voxelized by executing the `/voxelize` command inside Minecraft.
