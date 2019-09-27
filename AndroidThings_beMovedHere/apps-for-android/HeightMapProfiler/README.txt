HeightMapProfiler 1.0

Author: Chris Pruett
With help from: Dan Brunton

ABOUT

HeightMapProfiler is a simple application for testing the 3D rendering performance of various Android devices.  It builds a simple 3D landscape and allows the user to adjust the complexity of the scene.  Various OpenGL ES settings, such as texture size, mip mapping, texture filter, fixed point vertex arrays, and whether to use vertex buffer objects can be configured.  In 3D  mode you can drive around the landscape by touching and dragging your finger on the screen.  HeightMapProfiler includes simple collision detection with the ground.

The goal of this application is to allow Android developers to get a sense of what sort of scene complexity they should expect when designing 3D games for Android handsets.  The cost of extra vertices, as well as various texture sizes and other settings, can be tested with this application.  This is not a complete 3D benchmark--it's a very simple way to get a sense of the performance ceiling for basic OpenGL ES 1.0 rendering.

HeightMapProfiler also includes an option to turn on level-of-detail rendering.  This is a technique by which objects further from the camera are rendered at reduced complexity in order to lower the overall complexity of the visible scene.  This option is included here to show how much level of detail can help performance--for a slight (and, to most users, unnoticeable) reduction in scene quality for background objects, huge improvements in runtime performance can be achieved.  This is a very simple test (with programmatically generated programmer art, no less), but it should give you some idea of the power of this technique.

Finally, HeightMapProfiler is a useful sample for showing how to set up rendering with the NDK, generate a height map based on a 2D grayscale image, and do simple collision detection with that height map.

Note that HeightMapProfiler only tests rendering with OpenGL ES 1.0.  On devices that support GLES 2.0, you can expect performance to be the same or slightly better when rendering using GLES2.0, as such devices typically implement GLES 1.x support as an emulation layer on top of their 2.0-native hardware.


USAGE

Set various GLES properties as desired and click "Run Test" to start rendering.  Touch the screen to move around.  Hit the back button to return to the settings screen and get a report about performance.

Use the Mesh Complexity option under Mesh Settings to adjust the number of verts in each landsacpe tile.

Note that if you are looking for a reliable, reproducible benchmark, it's best to do the following:
  - Turn off "Big World" mode.
  - Turn off the skybox.
  - Run the test, but do not touch the screen.  After letting it run for 30 seconds or so, hit back to return to the previous screen and report the profiling results.
  
Also note that "Run Simulation" must be checked or the camera will not be properly initialized.  

Finally, when using the Native Rendering mode, the number of verts per frame will not be reported.


CONCLUSIONS

Briefly, here's what HeightMapProfiler tells us about Android 3D performance:

- There are two distinct classes of devices: first generation (G1, myTouch/Magic, etc) and second generation (Nexus One, Droid, etc).  The first generation devices can do about 5K verts per frame at 30 fps.  The second generation devices clock in at around 27k verts per frame at 30 fps, but are typically fill bound (big screens).

- Always, always use VBOs.  No need for fixed point, though.

- Level of detail, both in terms of mip mapping and simplified meshes, is a huge win.

- Rendering with the NDK is pointless if all you do is issue GLES commands (as this sample does).  If you're going to use the NDK, do something with it that is actually CPU intensive.  Just calling GL commands can be done at almost the same speed from Java.