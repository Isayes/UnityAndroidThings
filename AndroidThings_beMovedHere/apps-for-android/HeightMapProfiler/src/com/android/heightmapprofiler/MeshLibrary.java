/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.heightmapprofiler;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

// This is a central repository for all vertex arrays.  It handles
// generation and invalidation of VBOs from each mesh.

public class MeshLibrary {
	// This sample only has one type of mesh, Grid, but in a real game you might have
	// multiple types of objects managing vertex arrays (animated objects, objects loaded from
	// files, etc).  This class could easily be modified to work with some basic Mesh class
	// in that case.
	private ArrayList<Grid[]> mMeshes = new ArrayList<Grid[]>();
	
	public int addMesh(Grid[] lods) {
		int index = mMeshes.size();
		mMeshes.add(lods);
		return index;
	}
	
	public Grid[] getMesh(int index) {
		Grid[] mesh = null;
		if (index >= 0 && index < mMeshes.size()) {
			mesh = mMeshes.get(index);
		}
		return mesh;
	}
	
	public void generateHardwareBuffers(GL10 gl) {
		final int count = mMeshes.size();
		for (int x = 0; x < count; x++) {
			Grid[] lods = mMeshes.get(x);
			assert lods != null;
			for (int y = 0; y < lods.length; y++) {
				Grid lod = lods[y];
				if (lod != null) {
					lod.invalidateHardwareBuffers();
					lod.generateHardwareBuffers(gl);
				}
			}
		}
	}
	
	public void freeHardwareBuffers(GL10 gl) {
		final int count = mMeshes.size();
		for (int x = 0; x < count; x++) {
			Grid[] lods = mMeshes.get(x);
			assert lods != null;
			for (int y = 0; y < lods.length; y++) {
				Grid lod = lods[y];
				if (lod != null) {
					lod.releaseHardwareBuffers(gl);
				}
			}
		}
	}
}
