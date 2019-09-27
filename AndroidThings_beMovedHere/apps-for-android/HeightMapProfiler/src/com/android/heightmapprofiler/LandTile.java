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

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;

// This class defines a single land tile.  It is built from
// a height map image and may contain several meshes defining different levels
// of detail.
public class LandTile {
	public final static float TILE_SIZE = 512;
	private final static float HALF_TILE_SIZE = TILE_SIZE / 2;
	public final static float TILE_HEIGHT_THRESHOLD = 0.4f;
	private final static int MAX_SUBDIVISIONS = 24;
	private final static float LOD_STEP_SIZE = 300.0f;
	
	public final static int LOD_LEVELS = 4;
	private final static float MAX_LOD_DISTANCE = LOD_STEP_SIZE * (LOD_LEVELS - 1);
	private final static float MAX_LOD_DISTANCE2 = MAX_LOD_DISTANCE * MAX_LOD_DISTANCE;
	
	private Grid mLODMeshes[];
	private int mLODTextures[];
	private Vector3 mPosition = new Vector3();
	private Vector3 mCenterPoint = new Vector3();
	private Bitmap mHeightMap;
	private float mHeightMapScaleX;
	private float mHeightMapScaleY;
	private int mLodLevels = LOD_LEVELS;
	private int mMaxSubdivisions = MAX_SUBDIVISIONS;
	private float mTileSizeX = TILE_SIZE;
	private float mTileSizeZ = TILE_SIZE;
	private float mHalfTileSizeX = HALF_TILE_SIZE;
	private float mHalfTileSizeZ = HALF_TILE_SIZE;
	private float mTileHeightScale = TILE_HEIGHT_THRESHOLD;
	private float mMaxLodDistance = MAX_LOD_DISTANCE;
	private float mMaxLodDistance2 = MAX_LOD_DISTANCE2;
	
	public LandTile() {
	}
	
	public LandTile(boolean useLods, int maxSubdivisions ) {
		if (!useLods) {
			mLodLevels = 1;
		}
		mMaxSubdivisions = maxSubdivisions;
	}
	
	public LandTile(float sizeX, float sizeY, float sizeZ, int lodLevelCount, int maxSubdivisions, float maxLodDistance) {
		mTileSizeX = sizeX;
		mTileSizeZ = sizeZ;
		mTileHeightScale = (1.0f / 255.0f) * sizeY; 
		mLodLevels = lodLevelCount;
		mMaxSubdivisions = maxSubdivisions;
		mMaxLodDistance = maxLodDistance;
		mMaxLodDistance2 = maxLodDistance * maxLodDistance;
		mHalfTileSizeX = sizeX / 2.0f;
		mHalfTileSizeZ = sizeZ / 2.0f;
	}
	
	public void setLods(Grid[] lodMeshes, Bitmap heightmap) {
		mHeightMap = heightmap;
		mHeightMapScaleX = heightmap.getWidth() / mTileSizeX;
		mHeightMapScaleY = heightmap.getHeight() / mTileSizeZ;
		mLODMeshes = lodMeshes;
	}
	
	public void setLODTextures( int  LODTextures[] ) {
		mLODTextures = LODTextures;
	}
	
	public Grid[] generateLods(Bitmap heightmap, Bitmap lightmap, boolean useFixedPoint) {
		final int subdivisionSizeStep = mMaxSubdivisions / mLodLevels;
		mLODMeshes = new Grid[mLodLevels];
		for (int x = 0; x < mLodLevels; x++) {
			final int subdivisions = subdivisionSizeStep * (mLodLevels - x);
			mLODMeshes[x] = HeightMapMeshMaker.makeGrid(heightmap, lightmap, subdivisions, mTileSizeX, mTileSizeZ, mTileHeightScale, useFixedPoint);
		}
		
		mHeightMap = heightmap;
		mHeightMapScaleX = heightmap.getWidth() / mTileSizeX;
		mHeightMapScaleY = heightmap.getHeight() / mTileSizeZ;
		return mLODMeshes;
	}
	
	public final void setPosition(float x, float y, float z) {
		mPosition.set(x, y, z);
		mCenterPoint.set(x + mHalfTileSizeX, y, z + mHalfTileSizeZ);
	}
	 
	public final void setPosition(Vector3 position) {
		mPosition.set(position);
		mCenterPoint.set(position.x + mHalfTileSizeX, position.y, position.z + mHalfTileSizeZ);
	}
	
	public final Vector3 getPosition() {
		return mPosition;
	}
	
	public final Vector3 getCenterPoint() {
		return mCenterPoint;
	}
	
	public final Grid[] getLods() {
		return mLODMeshes;
	}
	
	public final float getMaxLodDistance() {
		return mMaxLodDistance;
	}
	
	public float getHeight(float worldSpaceX, float worldSpaceZ) {
		final float tileSpaceX = worldSpaceX - mPosition.x;
		final float tileSpaceY = worldSpaceZ - mPosition.z;
		final float imageSpaceX = tileSpaceX * mHeightMapScaleX;
		final float imageSpaceY = tileSpaceY * mHeightMapScaleY;
		float height = 0.0f;
		if (imageSpaceX >= 0.0f && imageSpaceX < mHeightMap.getWidth() 
				&& imageSpaceY >= 0.0f && imageSpaceY < mHeightMap.getHeight()) {
			height = HeightMapMeshMaker.getBilinearFilteredHeight(mHeightMap, imageSpaceX, imageSpaceY, mTileHeightScale);
		}
		return height;
	}
	
		
	public void draw(GL10 gl, Vector3 cameraPosition) {
		mCenterPoint.y = cameraPosition.y; 	// HACK!
		final float distanceFromCamera2 = cameraPosition.distance2(mCenterPoint);
		int lod = mLodLevels - 1;
		if (distanceFromCamera2 < mMaxLodDistance2) {
			final int bucket = (int)((distanceFromCamera2 / mMaxLodDistance2) * mLodLevels);
			lod = Math.min(bucket, mLodLevels - 1);
		}
		
		gl.glPushMatrix();
		gl.glTranslatef(mPosition.x, mPosition.y, mPosition.z);
		
		// TODO  - should add some code to keep state of current Texture and only set it if a new texture is needed - 
		// may be taken care of natively by OpenGL lib.
		if( mLODTextures != null ) {
			// Check to see if we have different LODs to choose from (i.e. Text LOD feature is turned on).  If not then
			// just select the default texture
			if(  mLODTextures.length == 1 ) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D,  mLODTextures[0]);
			}
			// if the LOD feature is enabled, use lod value to select correct texture to use
			else {
				gl.glBindTexture(GL10.GL_TEXTURE_2D,  mLODTextures[lod]);
			}
		}
		
		ProfileRecorder.sSingleton.addVerts(mLODMeshes[lod].getVertexCount());
		mLODMeshes[lod].draw(gl, true, true);
		
		gl.glPopMatrix();
	}

}
