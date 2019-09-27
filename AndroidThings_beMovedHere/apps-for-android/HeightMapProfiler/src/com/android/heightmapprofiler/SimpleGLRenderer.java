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

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * An OpenGL ES renderer based on the GLSurfaceView rendering framework.  This
 * class is responsible for drawing a list of renderables to the screen every
 * frame.  It also manages loading of textures and (when VBOs are used) the
 * allocation of vertex buffer objects.
 */
public class SimpleGLRenderer implements GLSurfaceView.Renderer {
	// Texture filtering modes.
	public final static int FILTER_NEAREST_NEIGHBOR = 0;
	public final static int FILTER_BILINEAR = 1;
	public final static int FILTER_TRILINEAR = 2;
	
    // Specifies the format our textures should be converted to upon load.
    private static BitmapFactory.Options sBitmapOptions
        = new BitmapFactory.Options();
    
    // Pre-allocated arrays to use at runtime so that allocation during the
    // test can be avoided.
    private int[] mTextureNameWorkspace;

    // A reference to the application context.
    private Context mContext;
    
    private LandTileMap mTiles;
    private int mTextureResource;
    private int mTextureResource2;

    private int mTextureId2;

    private Vector3 mCameraPosition = new Vector3();
    private Vector3 mLookAtPosition = new Vector3();
    private Object mCameraLock = new Object();
    private boolean mCameraDirty;
    
    private NativeRenderer mNativeRenderer;
    
    // Determines the use of vertex buffer objects.
    private boolean mUseHardwareBuffers;
    private boolean mUseTextureLods = false;
    private boolean mUseHardwareMips = false;
    boolean mColorTextureLods = false;
    private int mTextureFilter = FILTER_BILINEAR;
	private int mMaxTextureSize = 0; 
	



	public SimpleGLRenderer(Context context) {
        // Pre-allocate and store these objects so we can use them at runtime
        // without allocating memory mid-frame.
        mTextureNameWorkspace = new int[1];
        
        // Set our bitmaps to 16-bit, 565 format.
        sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        
        mContext = context;
        
        mUseHardwareBuffers = true;
    }
    
    public void setTiles(LandTileMap tiles, int textureResource, int textureResource2) {
    	mTextureResource = textureResource;
    	mTextureResource2 = textureResource2;
    	mTiles = tiles;
    }

    /** Draws the landscape. */
	public void onDrawFrame(GL10 gl) {
		ProfileRecorder.sSingleton.stop(ProfileRecorder.PROFILE_FRAME);
		ProfileRecorder.sSingleton.start(ProfileRecorder.PROFILE_FRAME);
		
        if (mTiles != null) {
        	// Clear the screen.  Note that a real application probably would only clear the depth buffer
        	// (or maybe not even that).
        	if (mNativeRenderer == null) {
                gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        	}
        	        	
        	// If the camera has moved since the last frame, rebuild our projection matrix.
        	if (mCameraDirty){
        		synchronized (mCameraLock) {
        			if (mNativeRenderer != null) {
        				mNativeRenderer.setCamera(mCameraPosition, mLookAtPosition);
        			} else {
			        	gl.glMatrixMode(GL10.GL_MODELVIEW);
			            gl.glLoadIdentity();
			            GLU.gluLookAt(gl, mCameraPosition.x, mCameraPosition.y, mCameraPosition.z, 
			            		mLookAtPosition.x, mLookAtPosition.y, mLookAtPosition.z, 
			            		0.0f, 1.0f, 0.0f);
        			}
		            mCameraDirty = false;
        		}
        	}
            
            ProfileRecorder.sSingleton.start(ProfileRecorder.PROFILE_DRAW);
            // Draw the landscape.
        	mTiles.draw(gl, mCameraPosition);
            ProfileRecorder.sSingleton.stop(ProfileRecorder.PROFILE_DRAW);

        	
        }
        
        ProfileRecorder.sSingleton.endFrame();
    }

    /* Called when the size of the window changes. */
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        /*
         * Set our projection matrix. This doesn't have to be done each time we
         * draw, but usually a new projection needs to be set when the viewport
         * is resized.
         */
        float ratio = (float)width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU.gluPerspective(gl, 60.0f, ratio, 2.0f, 3000.0f);

        mCameraDirty = true;  
    }
    
    public void setCameraPosition(float x, float y, float z) {
    	synchronized (mCameraLock) {
    		mCameraPosition.set(x, y, z);
    		mCameraDirty = true;
    	}
    }
    
    public void setCameraLookAtPosition(float x, float y, float z) {
    	synchronized (mCameraLock) {
    		mLookAtPosition.set(x, y, z);
    		mCameraDirty = true;
    	}
    }
    
    public void setUseHardwareBuffers(boolean vbos) {
    	mUseHardwareBuffers = vbos;
    }
    
    /**
     *  Called to turn on Levels of Detail option for rendering textures 
     *  
     * @param mUseTextureLods
     */
    public void setUseTextureLods(boolean useTextureLods, boolean useHardwareMips) {
		mUseTextureLods = useTextureLods;
		mUseHardwareMips = useHardwareMips;
	}
    
	/**
	 *  Turns on/off color the LOD bitmap textures based on their distance from view.
	 *  This feature is useful since it helps visualize the LOD being used while viewing the scene.
	 * 
	 * @param colorTextureLods
	 */
	public void setColorTextureLods(boolean colorTextureLods) {
		this.mColorTextureLods = colorTextureLods;
	}
	
    public void setNativeRenderer(NativeRenderer render) {
    	mNativeRenderer = render;
    }
    
    public void setMaxTextureSize(int maxTextureSize) {
		mMaxTextureSize = maxTextureSize;
	}
    
    public void setTextureFilter(int filter) {
    	mTextureFilter = filter;
    }

    /**
     * Called whenever the surface is created.  This happens at startup, and
     * may be called again at runtime if the device context is lost (the screen
     * goes to sleep, etc).  This function must fill the contents of vram with
     * texture data and (when using VBOs) hardware vertex arrays.
     */
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	int[] textureNames = null;
    	/*
         * Some one-time OpenGL initialization can be made here probably based
         * on features of this particular context
         */
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

        gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        gl.glDisable(GL10.GL_DITHER);
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_TEXTURE_2D);
         
        // set up some fog.
        gl.glEnable(GL10.GL_FOG);
        gl.glFogf(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
        float fogColor[] = { 0.5f, 0.5f, 0.5f, 1.0f };

        gl.glFogfv(GL10.GL_FOG_COLOR, fogColor, 0);
        gl.glFogf(GL10.GL_FOG_DENSITY, 0.15f);
        
        gl.glFogf(GL10.GL_FOG_START, 800.0f);
        gl.glFogf(GL10.GL_FOG_END, 2048.0f);

        gl.glHint(GL10.GL_FOG_HINT, GL10.GL_FASTEST);

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        // load textures and buffers here
        if (mUseHardwareBuffers && mTiles != null) {
        	mTiles.generateHardwareBuffers(gl);
        }
        
        if (mTextureResource != 0) {
        	textureNames =  loadBitmap( mContext, gl, mTextureResource, mUseTextureLods, mUseHardwareMips  );
        }
        
        if (mTextureResource2 != 0) {
        	mTextureId2 = loadBitmap(mContext, gl, mTextureResource2);
        }
        
        
        mTiles.setTextures(textureNames, mTextureId2);
    }
    
 
    /** 
     * Loads a bitmap into OpenGL and sets up the common parameters for 
     * 2D texture maps. 
     */
	protected int loadBitmap(Context context, GL10 gl, int resourceId) {
		int textureName = -1;

		InputStream is = context.getResources().openRawResource(resourceId);
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// Ignore.
			}
		}
		
		if (mMaxTextureSize > 0 && Math.max(bitmap.getWidth(), bitmap.getHeight()) != mMaxTextureSize) {
			// we're assuming all our textures are square.  sue me.
			Bitmap tempBitmap = Bitmap.createScaledBitmap( bitmap, mMaxTextureSize, mMaxTextureSize, false );
			bitmap.recycle();
			bitmap = tempBitmap;
		}

		textureName = loadBitmapIntoOpenGL(context, gl, bitmap, false);
		bitmap.recycle();
		return textureName;
	}

    /** 
     * Loads a bitmap into OpenGL and sets up the common parameters for 
     * 2D texture maps.   
     * 
     * @param context
     * @param resourceId
     * @param numLevelsOfDetail number of detail textures to generate 
     * 
     *  @return a array of OpenGL texture names corresponding to the different levels of detail textures
     */
	protected int[] loadBitmap(Context context, GL10 gl, int resourceId, boolean generateMips, boolean useHardwareMips ) {
		InputStream is = context.getResources().openRawResource(resourceId);
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// Ignore.
			}
		}
		
		if (mMaxTextureSize > 0 && Math.max(bitmap.getWidth(), bitmap.getHeight()) != mMaxTextureSize) {
			// we're assuming all our textures are square.  sue me.
			Bitmap tempBitmap = Bitmap.createScaledBitmap( bitmap, mMaxTextureSize, mMaxTextureSize, false );
			bitmap.recycle();
			bitmap = tempBitmap;
		}
		
		final int minSide = Math.min(bitmap.getWidth(), bitmap.getHeight());
		int numLevelsOfDetail = 1;
		if (generateMips) {
			for (int side = minSide / 2; side > 0; side /= 2) {
				numLevelsOfDetail++;
			}
		}
		
		int textureNames[];
		if (generateMips && !useHardwareMips) {
			textureNames = new int[numLevelsOfDetail];
		} else {
			textureNames = new int[1];
		}

		textureNames[0] = loadBitmapIntoOpenGL(context, gl, bitmap, useHardwareMips);
		// Scale down base bitmap by powers of two to create lower resolution textures 
		for( int i = 1; i <  numLevelsOfDetail; ++i ) {
			int scale =  (int)Math.pow(2, i);
			int dstWidth = bitmap.getWidth()  / scale;
			int dstHeight = bitmap.getHeight() / scale;
			Bitmap tempBitmap = Bitmap.createScaledBitmap( bitmap, dstWidth, dstHeight, false );
			// Set each LOD level to a different color to help visualization 
			if(  mColorTextureLods  ) {
				int color = 0;
				switch( (int)(i % 4) ) {
				case 1:
					color = Color.RED;
					break;
				case 2:
					color = Color.YELLOW;
					break;
				case 3:
					color = Color.BLUE;
					break;
				}

				tempBitmap.eraseColor( color );
			}
			
			if (!useHardwareMips) {
				textureNames[i] = loadBitmapIntoOpenGL( context, gl, tempBitmap, false);
			} else {
				addHardwareMipmap(gl, textureNames[0], tempBitmap, i );
			}
			tempBitmap.recycle();
		}
		
		bitmap.recycle();
		return textureNames;
	}
	

	protected void addHardwareMipmap(GL10 gl, int textureName, Bitmap bitmap, int level) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
	}
	
	
    /** 
     * Loads a bitmap into OpenGL and sets up the common parameters for 
     * 2D texture maps. 
     * 
     * @return OpenGL texture entry id
     */
    protected int loadBitmapIntoOpenGL(Context context, GL10 gl,  Bitmap bitmap, boolean useMipmaps) {
        int textureName = -1;
        if (context != null && gl != null) {
            gl.glGenTextures(1, mTextureNameWorkspace, 0);

            textureName = mTextureNameWorkspace[0];
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
            
            if (useMipmaps) {
            	if (mTextureFilter == FILTER_TRILINEAR) {
            		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_LINEAR);
            	} else {
            		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
            	}
            } else {
            	if (mTextureFilter == FILTER_NEAREST_NEIGHBOR) {
            		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            	} else {
            		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            	}
            }
            
            if (mTextureFilter == FILTER_NEAREST_NEIGHBOR) {
            	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
            } else {
            	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            }

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

            gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            int error = gl.glGetError();
            if (error != GL10.GL_NO_ERROR) {
                Log.e("SimpleGLRenderer", "Texture Load GLError: " + error);
            }
        
        }

        return textureName;
    }

	

}
