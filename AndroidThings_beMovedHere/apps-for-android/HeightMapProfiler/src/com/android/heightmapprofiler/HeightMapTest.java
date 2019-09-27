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

import android.opengl.GLSurfaceView;

import com.android.heightmapprofiler.SimpleGLRenderer;
import com.android.heightmapprofiler.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MotionEvent;

// The main entry point for the actual landscape rendering test.
// This class pulls options from the preferences set in the MainMenu
// activity and builds the landscape accordingly.
public class HeightMapTest extends Activity {
    private GLSurfaceView mGLSurfaceView;
    private SimpleGLRenderer mSimpleRenderer;
    private Game mGame;
    private Thread mGameThread;
    private float mLastScreenX;
    private float mLastScreenY;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(this);
        mGLSurfaceView.setEGLConfigChooser(true);
        
        mSimpleRenderer = new SimpleGLRenderer(this);
        
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean runGame = prefs.getBoolean("runsim", true);
        final boolean bigWorld = prefs.getBoolean("bigworld", true);
        final boolean skybox = prefs.getBoolean("skybox", true);
        final boolean texture = prefs.getBoolean("texture", true);
        final boolean vertexColors = prefs.getBoolean("colors", true);
        final boolean useTextureLods = prefs.getBoolean("lodTexture", true);
        final boolean textureMips = prefs.getBoolean("textureMips", true);
        final boolean useColorTextureLods = prefs.getBoolean("lodTextureColored", false );
        final int maxTextureSize = Integer.parseInt(prefs.getString("maxTextureSize", "512"));
        final String textureFilter = prefs.getString("textureFiltering", "bilinear");
        final boolean useLods = prefs.getBoolean("lod", true);
        final int complexity = Integer.parseInt(prefs.getString("complexity", "24"));
        final boolean useFixedPoint = prefs.getBoolean("fixed", false);
        final boolean useVbos = prefs.getBoolean("vbo", true);
        final boolean useNdk = prefs.getBoolean("ndk", false);


        BitmapDrawable heightMapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.heightmap);
    	Bitmap heightmap = heightMapDrawable.getBitmap();
    	
    	Bitmap lightmap = null;
    	if (vertexColors != false) {
    		BitmapDrawable lightMapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.lightmap);
    		lightmap = lightMapDrawable.getBitmap();
    	}
    	
    	LandTileMap tileMap = null;
    	if (bigWorld) {
	    	
	    	
	    	final int tilesX = 4;
	    	final int tilesY = 4;
	    	tileMap = new LandTileMap(tilesX, tilesY, heightmap, lightmap, vertexColors, texture, useLods, complexity, useFixedPoint);

    	} else {
    		tileMap = new LandTileMap(1, 1, heightmap, lightmap, vertexColors, texture, useLods, complexity, useFixedPoint);
    	}
    	
    	if (skybox) {
    		BitmapDrawable skyboxDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.skybox);
	    	Bitmap skyboxBitmap = skyboxDrawable.getBitmap();
	    	tileMap.setupSkybox(skyboxBitmap, useFixedPoint);
    	}
    	
    	
    	if (useNdk) {
    		NativeRenderer renderer = new NativeRenderer();
    		tileMap.setNativeRenderer(renderer);
    		mSimpleRenderer.setNativeRenderer(renderer);
    	}
        
    	
    	ProfileRecorder.sSingleton.resetAll();
    	mSimpleRenderer.setUseHardwareBuffers(useVbos);
    	if (texture) {
    		mSimpleRenderer.setTiles(tileMap, R.drawable.road_texture, R.drawable.skybox_texture);
    		mSimpleRenderer.setUseTextureLods(  useTextureLods, textureMips );
    		mSimpleRenderer.setColorTextureLods( useColorTextureLods );
    		mSimpleRenderer.setMaxTextureSize(maxTextureSize);
    		if (textureFilter == "nearest") {
    			mSimpleRenderer.setTextureFilter(SimpleGLRenderer.FILTER_NEAREST_NEIGHBOR);
    		} else if (textureFilter == "trilinear") {
    			mSimpleRenderer.setTextureFilter(SimpleGLRenderer.FILTER_TRILINEAR);
    		} else {
    			mSimpleRenderer.setTextureFilter(SimpleGLRenderer.FILTER_BILINEAR);
    		}
    			
    	} else {
    		mSimpleRenderer.setTiles(tileMap, 0, 0);

    	}
    	mGLSurfaceView.setRenderer(mSimpleRenderer);
        setContentView(mGLSurfaceView);
        
        if (runGame) {
    		mGame = new Game(mSimpleRenderer, tileMap);
    		mGameThread = new Thread(mGame);
    		mGameThread.start();
        }
        
        

    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
        if (mGame != null) {
        	mGame.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
        if (mGame != null) {
	        mGame.resume();
        }
    }
    
    @Override
	public boolean onTrackballEvent(MotionEvent event) {
        if (mGame != null) {
        	mGame.rotate(event.getRawX(), -event.getRawY());
        }
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
        if (mGame != null) {
        	if (event.getAction() == MotionEvent.ACTION_DOWN) {
        		mGame.move(1.0f);
        	} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
        		float xDelta = event.getX() - mLastScreenX;
        		float yDelta = event.getY() - mLastScreenY;
        		mGame.move(1.0f);
        		// Scale the values we got down to make control usable.
        		// A real game would probably figure out scale factors based
        		// on the size of the screen rather than hard-coded constants
        		// like this.
		    	mGame.rotate(xDelta * 0.01f, -yDelta * 0.005f);
        	}
        }
        mLastScreenX = event.getX();
        mLastScreenY = event.getY();
		try {
			Thread.sleep(16);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		return true;
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mGame != null) {
			final float speed = 1.0f;
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN: 
		    	mGame.rotate(0.0f, -speed);
	
				return true; 
			case KeyEvent.KEYCODE_DPAD_LEFT: 
		    	mGame.rotate(-speed, 0.0f);
	
				return true; 
			case KeyEvent.KEYCODE_DPAD_RIGHT: 
		    	mGame.rotate(speed, 0.0f);
	
				return true; 
			case KeyEvent.KEYCODE_DPAD_UP: 
		    	mGame.rotate(0.0f, speed);
	
				return true; 
			} 
        }
		return super.onKeyDown(keyCode, event);
	}
    

}