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

import android.graphics.Bitmap;
import android.graphics.Color;

// This class generates vertex arrays based on grayscale images.
// It defines 1.0 (white) as the tallest point and 0.0 (black) as the lowest point,
// and builds a mesh that represents that topology.
public class HeightMapMeshMaker {

	public static final Grid makeGrid(Bitmap drawable, Bitmap lightmap, int subdivisions, float width, float height, float scale, boolean fixedPoint) {
    	Grid grid = null;
 
    	final float subdivisionRange = subdivisions - 1;
    	
    	final float vertexSizeX = width / subdivisionRange;
    	final float vertexSizeZ = height / subdivisionRange;
    	
    	if (drawable != null) {
    		grid = new Grid(subdivisions, subdivisions, fixedPoint);
    		final float heightMapScaleX = drawable.getWidth() / subdivisionRange;
    		final float heightMapScaleY = drawable.getHeight() / subdivisionRange;
    		final float lightMapScaleX = lightmap != null ? lightmap.getWidth() / subdivisions : 0.0f;
    		final float lightMapScaleY = lightmap != null ? lightmap.getHeight() / subdivisions : 0.0f;
    		
    		final float[] vertexColor = { 1.0f, 1.0f, 1.0f, 1.0f };
    		for (int i = 0; i < subdivisions; i++) {
    			final float u = (float)(i + 1) / subdivisions;
    			for (int j = 0; j < subdivisions; j++) {
    				final float v = (float)(j + 1) / subdivisions;
    	
    				final float vertexHeight = getBilinearFilteredHeight(drawable, (heightMapScaleX * i), (heightMapScaleY * j), scale);
    				
    				if (lightmap != null) {
    					final int lightColor = lightmap.getPixel((int)(lightMapScaleX * i), (int)(lightMapScaleY * j));
    					final float colorScale = 1.0f / 255.0f;
    					vertexColor[0] = colorScale * Color.red(lightColor);
    					vertexColor[1] = colorScale * Color.green(lightColor);
    					vertexColor[2] = colorScale * Color.blue(lightColor);
    					vertexColor[3] = colorScale * Color.alpha(lightColor);
    					
    				}
    				grid.set(i, j, i * vertexSizeX, vertexHeight, j * vertexSizeZ, u, v, vertexColor);
    			}
    		}
      	}
    	return grid;
    }
	
	// In order to get a smooth gradation between pixels from a low-resolution height map,
	// this function uses a bilinear filter to calculate a weighted average of four pixels
	// surrounding the requested point.
	public static final float getBilinearFilteredHeight(Bitmap drawable, float x, float y, float scale) {
		final int topLeftPixelX = clamp((int)Math.floor(x), 0, drawable.getWidth() - 1);
		final int topLeftPixelY = clamp((int)Math.floor(y), 0, drawable.getHeight() - 1);
		final int bottomRightPixelX = clamp((int)Math.ceil(x), 0, drawable.getWidth() - 1);
		final int bottomRightPixelY = clamp((int)Math.ceil(y), 0, drawable.getHeight() - 1);
		final float topLeftWeightX = x - topLeftPixelX;
		final float topLeftWeightY = y - topLeftPixelY;
		final float bottomRightWeightX = 1.0f - topLeftWeightX;
		final float bottomRightWeightY = 1.0f - topLeftWeightY;
		
		final int topLeft = drawable.getPixel(topLeftPixelX, topLeftPixelY);
		final int topRight = drawable.getPixel(bottomRightPixelX, topLeftPixelY);
		final int bottomLeft = drawable.getPixel(topLeftPixelX, bottomRightPixelY);
		final int bottomRight = drawable.getPixel(bottomRightPixelX, bottomRightPixelY);
		
		final float red1 = bottomRightWeightX * Color.red(topLeft) + topLeftWeightX * Color.red(topRight);
		final float red2 = bottomRightWeightX * Color.red(bottomLeft) + topLeftWeightX * Color.red(bottomRight);
		final float red = bottomRightWeightY * red1 + topLeftWeightY * red2;
		
		final float height = red * scale;
		return height;
		
	}
	
	private static final int clamp(int value, int min, int max) {
		return value < min ? min : (value > max ? max : value);
	}
}
