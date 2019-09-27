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

// This is a thin interface for a renderer implemented in C++ using the NDK.

public class NativeRenderer {
	private Vector3 mCameraPosition = new Vector3();
	private Vector3 mLookAtPosition = new Vector3();
	private boolean mCameraDirty = false;
	
	static {
        System.loadLibrary("heightmapprofiler");
    }
	
	public NativeRenderer() {
		nativeReset();
	}
	
	public void setCamera(Vector3 camera, Vector3 lookat) {
		mCameraPosition = camera;
		mLookAtPosition = lookat;
		mCameraDirty = true;
	}
	
	public void registerTile(int textures[], LandTile tile, boolean isSkybox) {
		final Grid[] lods = tile.getLods();
		final Vector3 position = tile.getPosition();
		final Vector3 center = tile.getCenterPoint();
		final int index = nativeAddTile(textures[0], lods.length, tile.getMaxLodDistance(), position.x, position.y, position.z, center.x, center.y, center.z);
		if (index >= 0) {
			for (int x = 0; x < lods.length; x++) {
				nativeAddLod(index, lods[x].getVertexBuffer(), lods[x].getTextureBuffer(), lods[x].getIndexBuffer(), lods[x].getColorBuffer(), lods[x].getIndexCount(), lods[x].getFixedPoint());
			}
			if (isSkybox) {
				nativeSetSkybox(index);
			}
    		
    		for( int i = 1; i < textures.length; ++i ) {
    			nativeAddTextureLod( index, i, textures[i]);
    		}
		}
	}
	
	public void registerSkybox(int texture, Grid mesh, Vector3 position, Vector3 centerPoint) {
		
	}
	
	public void draw(boolean useTexture, boolean useColor) {
		nativeRender(mCameraPosition.x, mCameraPosition.y, mCameraPosition.z, mLookAtPosition.x, mLookAtPosition.y, mLookAtPosition.z, useTexture, useColor, mCameraDirty);
		mCameraDirty = false;
	}
	
	private static native void nativeReset();
	private static native int nativeAddTile(int texture, int lodCount, float maxLodDistance, float x, float y, float z, float centerX, float centerY, float centerZ);
	private static native void nativeAddLod(int index, int vertexBuffer, int textureBuffer, int indexBuffer, int colorBuffer, int indexCount, boolean useFixedPoint);
	private static native void nativeSetSkybox(int index);
	private static native void nativeRender(float cameraX, float cameraY, float cameraZ, float lookAtX, float lookAtY, float lookAtZ, boolean useTexture, boolean useColor, boolean cameraDirty);
	private static native void nativeAddTextureLod(int tileIndex, int lod, int textureName);
}
