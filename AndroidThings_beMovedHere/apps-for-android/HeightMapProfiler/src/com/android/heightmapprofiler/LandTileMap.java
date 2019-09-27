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

// This class manages a regular grid of LandTile objects.  In this sample,
// all the objects are the same mesh.  In a real game, they would probably be
// different to create a more interesting landscape.
// This class also abstracts the concept of tiles away from the rest of the
// code, so that the collision system (amongst others) can query the height of any
// given point in the world.
public class LandTileMap {
	private MeshLibrary mMeshLibrary = new MeshLibrary();
	private LandTile[] mTiles;
	private LandTile mSkybox;
	private float mWorldWidth;
	private float mWorldHeight;
	private int mTilesAcross;
	private int mSkyboxTexture;
	private boolean mUseColors;
	private boolean mUseTexture;
	private NativeRenderer mNativeRenderer;
	
	public LandTileMap(
			int tilesAcross, 
			int tilesDown, 
			Bitmap heightmap, 
			Bitmap lightmap, 
			boolean useColors,
			boolean useTexture,
			boolean useLods, 
			int maxSubdivisions,
			boolean useFixedPoint) {
		Grid[] lodMeshes;
		int lodLevels = 1;
		if (useLods) {
			lodLevels = LandTile.LOD_LEVELS;
		}
		lodMeshes = new Grid[lodLevels];
		final int subdivisionSizeStep = maxSubdivisions / lodLevels;
		for (int x = 0; x < lodLevels; x++) {
			final int subdivisions = subdivisionSizeStep * (lodLevels - x);
			lodMeshes[x] = HeightMapMeshMaker.makeGrid(
					heightmap, 
					lightmap, 
					subdivisions, 
					LandTile.TILE_SIZE, 
					LandTile.TILE_SIZE, 
					LandTile.TILE_HEIGHT_THRESHOLD, 
					useFixedPoint);
		}
		
		mMeshLibrary.addMesh(lodMeshes);

		LandTile[] tiles = new LandTile[tilesAcross * tilesDown];
    	for (int x = 0; x < tilesAcross; x++) {
    		for (int y = 0; y < tilesDown; y++) {
	    		LandTile tile = new LandTile(useLods, maxSubdivisions);
	            tile.setLods(lodMeshes, heightmap);
	            tiles[x * tilesAcross + y] = tile;
	            tile.setPosition(x * LandTile.TILE_SIZE, 0.0f, y * LandTile.TILE_SIZE);
	    	}
    	}
    	mTiles = tiles;
    	mWorldWidth = tilesAcross * LandTile.TILE_SIZE;
    	mWorldHeight = tilesDown * LandTile.TILE_SIZE;
    	mTilesAcross = tilesAcross;
    	
    	mUseColors = useColors;
    	mUseTexture = useTexture;
	}
	
	public void setLandTextures( int  landTextures[] )  {		
		for( LandTile landTile : mTiles ) {
			landTile.setLODTextures( landTextures );
		}
	}
	
	public void setupSkybox(Bitmap heightmap, boolean useFixedPoint) {
		if (mSkybox == null) {
			mSkybox = new LandTile(mWorldWidth, 1024, mWorldHeight, 1, 16, 1000000.0f);
			mMeshLibrary.addMesh(mSkybox.generateLods(heightmap, null, useFixedPoint));
			mSkybox.setPosition(0.0f, 0.0f, 0.0f);
		}
	}
	
	public float getHeight(float worldX, float worldZ) {
		float height = 0.0f;
		if (worldX > 0.0f && worldX < mWorldWidth && worldZ > 0.0f && worldZ < mWorldHeight) {
			int tileX = (int)(worldX / LandTile.TILE_SIZE);
			int tileY = (int)(worldZ / LandTile.TILE_SIZE);
			
			height = mTiles[tileX * mTilesAcross + tileY].getHeight(worldX, worldZ);
		}
		return height;
	}
	
	public void setTextures(int[] landTextures, int skyboxTexture) {
		setLandTextures(  landTextures );
		mSkyboxTexture = skyboxTexture;
		
		if (mNativeRenderer != null) {
			final int count = mTiles.length;
	    	for (int x = 0; x < count; x++) {
	    		mNativeRenderer.registerTile(landTextures, mTiles[x], false);
	    	} 
	    	
	    	if (mSkybox != null) {
	    		// Work around since registerTile() takes an array of textures
	    		int textures[] = new int[1];
	    		textures[0] = skyboxTexture;
	    		mNativeRenderer.registerTile(textures, mSkybox, true);
	    	}
		}
	}
	
	public void draw(GL10 gl, Vector3 cameraPosition) {
		if (mNativeRenderer != null) {
			mNativeRenderer.draw(true, true);
		} else {
			if (mSkyboxTexture != 0) {
	            gl.glBindTexture(GL10.GL_TEXTURE_2D, mSkyboxTexture);
	    	}
	      
			Grid.beginDrawing(gl, mUseTexture, mUseColors);
			if (mSkybox != null) {
				gl.glDepthMask(false);
				gl.glDisable(GL10.GL_DEPTH_TEST);
				mSkybox.draw(gl, cameraPosition);
				gl.glDepthMask(true);
				gl.glEnable(GL10.GL_DEPTH_TEST);
			}
			
	    	final int count = mTiles.length;
	    	for (int x = 0; x < count; x++) {
	        	mTiles[x].draw(gl, cameraPosition);
	    	} 
	    	
	    	Grid.endDrawing(gl);
		}
	}
	
	public void generateHardwareBuffers(GL10 gl) {
		mMeshLibrary.generateHardwareBuffers(gl);
	}

	public void setNativeRenderer(NativeRenderer nativeRenderer) {
		mNativeRenderer = nativeRenderer;
		
	}
	
}
