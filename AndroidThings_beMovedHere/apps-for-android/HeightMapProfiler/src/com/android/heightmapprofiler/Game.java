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

import android.os.SystemClock;

// Very simple game runtime.  Implements basic movement and collision detection with the landscape.
public class Game implements Runnable {

	private Vector3 mCameraPosition = new Vector3(100.0f, 128.0f, 400.0f);
    private Vector3 mTargetPosition = new Vector3(350.0f, 128.0f, 650.0f);
    private Vector3 mWorkVector = new Vector3();
    private float mCameraXZAngle;
    private float mCameraYAngle;
    private float mCameraLookAtDistance = (float)Math.sqrt(mTargetPosition.distance2(mCameraPosition));
    private boolean mCameraDirty = true;
    private boolean mRunning = true;
    private boolean mPaused = false;
    private Object mPauseLock = new Object();
    private LandTileMap mTileMap;
    
    private final static float CAMERA_ORBIT_SPEED = 0.3f;
    private final static float CAMERA_MOVE_SPEED = 5.0f;
    private final static float VIEWER_HEIGHT = 15.0f;
    
    private SimpleGLRenderer mSimpleRenderer;
    
	public Game(SimpleGLRenderer renderer, LandTileMap tiles) {
		mSimpleRenderer = renderer;
		mTileMap = tiles;
	}

	public void run() {
		while (mRunning) {
			ProfileRecorder.sSingleton.start(ProfileRecorder.PROFILE_SIM);
			long startTime = SystemClock.uptimeMillis();
			
			if (mCameraDirty) {
				// snap the camera to the floor
				float height = mTileMap.getHeight(mCameraPosition.x, mCameraPosition.z);
				mCameraPosition.y = height + VIEWER_HEIGHT;
				updateCamera();
			}
			
			long endTime = SystemClock.uptimeMillis();
			ProfileRecorder.sSingleton.stop(ProfileRecorder.PROFILE_SIM);
			
			if (endTime - startTime < 16) { 
				// we're running too fast!  sleep for a bit to let the render thread do some work.
				try {
					Thread.sleep(16 - (endTime - startTime));
				} catch (InterruptedException e) {
					// Interruptions are not a big deal here.
				}
			}
			synchronized(mPauseLock) {
				if (mPaused) {
					while (mPaused) {
						try {
							mPauseLock.wait();
						} catch (InterruptedException e) {
							// OK if this is interrupted.
						}
					}
				}
			}
		}
	}
	
	synchronized private void updateCamera() {
		mWorkVector.set((float)Math.cos(mCameraXZAngle), (float)Math.sin(mCameraYAngle), (float)Math.sin(mCameraXZAngle));
		mWorkVector.multiply(mCameraLookAtDistance);
		mWorkVector.add(mCameraPosition);
    	
    	mTargetPosition.set(mWorkVector);
		mSimpleRenderer.setCameraPosition(mCameraPosition.x, mCameraPosition.y, mCameraPosition.z);
		mSimpleRenderer.setCameraLookAtPosition(mTargetPosition.x, mTargetPosition.y, mTargetPosition.z);
		mCameraDirty = false;
	}
	
	synchronized public void rotate(float x, float y) {
		if (x != 0.0f) {
			mCameraXZAngle += x * CAMERA_ORBIT_SPEED;
			mCameraDirty = true;
		}
		
		if (y != 0.0f) {
			mCameraYAngle += y * CAMERA_ORBIT_SPEED;
			mCameraDirty = true;
		}
	}

	synchronized public void move(float amount) {
		mWorkVector.set(mTargetPosition);
		mWorkVector.subtract(mCameraPosition);
		mWorkVector.normalize();
		mWorkVector.multiply(amount * CAMERA_MOVE_SPEED);
		mCameraPosition.add(mWorkVector);
		mTargetPosition.add(mWorkVector);
		mCameraDirty = true;
	}
	
	public void pause() {
		synchronized(mPauseLock) {
			mPaused = true;
		}
	}
	
	public void resume() {
		synchronized(mPauseLock) {
			mPaused = false;
            mPauseLock.notifyAll();
		}
	}

}
