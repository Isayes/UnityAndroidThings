/*
 * Copyright (C) 2009 The Android Open Source Project
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
#include <assert.h>
#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <stdint.h>
#include <math.h>

#include <GLES/gl.h>

static const char* LogTitle = "HeightMapProfiler";

#ifdef DEBUG
#define ASSERT(x) if (!(x)) { __assert(__FILE__, __LINE__, #x); }
#else
#define ASSERT(X)
#endif


#ifdef REPORTING
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LogTitle, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LogTitle, __VA_ARGS__)
#else
#define LOGE(...)
#define LOGI(...)
#endif

void __assert(const char* pFileName, const int line, const char* pMessage) {
	__android_log_print(ANDROID_LOG_ERROR, "HeightMapProfiler", "Assert Failed!  %s  (%s:%d)", pMessage, pFileName, line);
	assert(false);
}

struct Mesh {
	int vertexBuffer;
	int textureBuffer;
	int colorBuffer;
	int indexBuffer;
	int indexCount;
	bool useFixedPoint;
};

struct Tile {
	 // Store a texture value for each texture LOD (Level of Detail)
	int textures[4];
	float x;
	float y;
	float z;
	float centerX;
	float centerY;
	float centerZ;
	int maxLodCount;
	int lodCount;
	Mesh lods[4];
	float maxLodDistance2;
};

static const int MAX_TILES = 17; //4x4 tiles + 1 skybox.  This really shouldn't be hard coded this way though.

static int sTileCount = 0;
static Tile sTiles[MAX_TILES];
static int sSkybox = -1;

static void nativeReset(JNIEnv*  env, jobject thiz) {
	sTileCount = 0;
	sSkybox = -1;
}
//	static native int nativeAddTile(int texture, int lodCount, float maxLodDistance, float x, float y, float z, float centerX, float centerY, float centerZ);
static jint nativeAddTile(
		JNIEnv*  env,
		jobject thiz,
		jint texture,
		jint lodCount,
		jfloat maxLodDistance,
		jfloat x,
		jfloat y,
		jfloat z,
		jfloat centerX,
		jfloat centerY,
		jfloat centerZ) {
	LOGI("nativeAddTile");

	int index = -1;
	if (sTileCount < MAX_TILES) {
		index = sTileCount;
		Tile* currentTile = &sTiles[index];
		currentTile->x = x;
		currentTile->y = y;
		currentTile->z = z;
		currentTile->centerX = centerX;
		currentTile->centerY = centerY;
		currentTile->centerZ = centerZ;
		currentTile->lodCount = 0;
		currentTile->maxLodCount = lodCount;
		currentTile->maxLodDistance2 = maxLodDistance * maxLodDistance;
		// Texture array size hard coded for now
		currentTile->textures[0] = texture;  // first element takes default LOD texture
		currentTile->textures[1] = -1;  // rest are set by nativeAddTextureLod() call
		currentTile->textures[2] = -1;
		currentTile->textures[3] = -1;
		sTileCount++;

		LOGI("Tile %d: (%g, %g, %g).  Max lod: %d", index, x, y, z, lodCount);
	}

	return index;
}

static void nativeSetSkybox(
		JNIEnv*  env,
		jobject thiz,
		jint index) {
	if (index < sTileCount && index >= 0) {
		sSkybox = index;
	}
}

//	static native void nativeAddLod(int index, int vertexBuffer, int textureBuffer, int indexBuffer, int colorBuffer, int indexCount, boolean useFixedPoint);
static void nativeAddLod(
		JNIEnv*  env,
		jobject thiz,
		jint index,
		jint vertexBuffer,
		jint textureBuffer,
		jint indexBuffer,
		jint colorBuffer,
		jint indexCount,
		jboolean useFixedPoint) {

	LOGI("nativeAddLod (%d)", index);

	if (index < sTileCount && index >= 0) {
		Tile* currentTile = &sTiles[index];
		LOGI("Adding lod for tile %d (%d of %d) - %s", index, currentTile->lodCount, currentTile->maxLodCount, useFixedPoint ? "fixed" : "float");

		if (currentTile->lodCount < currentTile->maxLodCount) {

			const int meshIndex = currentTile->lodCount;
			LOGI("Mesh %d: Index Count: %d", meshIndex, indexCount);

			Mesh* lod = &currentTile->lods[meshIndex];
			lod->vertexBuffer = vertexBuffer;
			lod->textureBuffer = textureBuffer;
			lod->colorBuffer = colorBuffer;
			lod->indexBuffer = indexBuffer;
			lod->indexCount = indexCount;
			lod->useFixedPoint = useFixedPoint;
			currentTile->lodCount++;

		}
	}
}

//	static native void nativeAddTextureLod(int index, int lod, int textureName);
static void nativeAddTextureLod(
		JNIEnv*  env,
		jobject thiz,
		jint tileIndex,
		jint lod,
		jint textureName) {


	if (tileIndex < sTileCount && tileIndex >= 0) {
		Tile* currentTile = &sTiles[tileIndex];
		LOGI("Adding texture lod for tile %d  lod %d texture name %d", tileIndex, lod, textureName);
		if( lod >=  sizeof(currentTile->textures)/sizeof(currentTile->textures[0]) ) {
			LOGI("Error adding  texture lod for tile %d  lod %d is out of range ", tileIndex, lod );
		}

		currentTile->textures[lod] = textureName;
	}
}


static void drawTile(int index, float cameraX, float cameraY, float cameraZ, bool useTexture, bool useColor) {
	//LOGI("draw tile: %d", index);

	if (index < sTileCount) {
		Tile* currentTile = &sTiles[index];
		const float dx = currentTile->centerX - cameraX;
		const float dz = currentTile->centerZ - cameraZ;
		const float distanceFromCamera2 = (dx * dx) + (dz * dz);
		int lod = currentTile->lodCount - 1;

		if (distanceFromCamera2 < currentTile->maxLodDistance2) {
			const int bucket = (int)((distanceFromCamera2 / currentTile->maxLodDistance2) * currentTile->lodCount);
			lod = bucket < (currentTile->lodCount - 1) ? bucket : currentTile->lodCount - 1;
		}

		ASSERT(lod < currentTile->lodCount);

		Mesh* lodMesh = &currentTile->lods[lod];

		//LOGI("mesh %d: count: %d", index, lodMesh->indexCount);

		const GLint coordinateType = lodMesh->useFixedPoint ? GL_FIXED : GL_FLOAT;

		glPushMatrix();
		glTranslatef(currentTile->x, currentTile->y, currentTile->z);

		glBindBuffer(GL_ARRAY_BUFFER, lodMesh->vertexBuffer);
		glVertexPointer(3, coordinateType, 0, 0);

		if (useTexture) {
			int texture = currentTile->textures[0];
			if( currentTile->textures[lod] != -1 ) {
				texture = currentTile->textures[lod];
			}
			glBindTexture(GL_TEXTURE_2D,  texture);
			glBindBuffer(GL_ARRAY_BUFFER, lodMesh->textureBuffer);
			glTexCoordPointer(2, coordinateType, 0, 0);
		}

		if (useColor) {
			glBindBuffer(GL_ARRAY_BUFFER, lodMesh->colorBuffer);
			glColorPointer(4, coordinateType, 0, 0);
		}

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, lodMesh->indexBuffer);
		glDrawElements(GL_TRIANGLES, lodMesh->indexCount,
				GL_UNSIGNED_SHORT, 0);


		glPopMatrix();
	}
}

// Blatantly copied from the GLU ES project:
// http://code.google.com/p/glues/
static void __gluMakeIdentityf(GLfloat m[16])
{
	m[0+4*0] = 1; m[0+4*1] = 0; m[0+4*2] = 0; m[0+4*3] = 0;
	m[1+4*0] = 0; m[1+4*1] = 1; m[1+4*2] = 0; m[1+4*3] = 0;
	m[2+4*0] = 0; m[2+4*1] = 0; m[2+4*2] = 1; m[2+4*3] = 0;
	m[3+4*0] = 0; m[3+4*1] = 0; m[3+4*2] = 0; m[3+4*3] = 1;
}

static void normalize(GLfloat v[3])
{
    GLfloat r;

    r=(GLfloat)sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
    if (r==0.0f)
    {
        return;
    }

    v[0]/=r;
    v[1]/=r;
    v[2]/=r;
}

static void cross(GLfloat v1[3], GLfloat v2[3], GLfloat result[3])
{
    result[0] = v1[1]*v2[2] - v1[2]*v2[1];
    result[1] = v1[2]*v2[0] - v1[0]*v2[2];
    result[2] = v1[0]*v2[1] - v1[1]*v2[0];
}

static void gluLookAt(GLfloat eyex, GLfloat eyey, GLfloat eyez, GLfloat centerx,
     GLfloat centery, GLfloat centerz, GLfloat upx, GLfloat upy,
     GLfloat upz)
{
	GLfloat forward[3], side[3], up[3];
	GLfloat m[4][4];

	forward[0] = centerx - eyex;
	forward[1] = centery - eyey;
	forward[2] = centerz - eyez;

	up[0] = upx;
	up[1] = upy;
	up[2] = upz;

	normalize(forward);

	/* Side = forward x up */
	cross(forward, up, side);
	normalize(side);

	/* Recompute up as: up = side x forward */
	cross(side, forward, up);

	__gluMakeIdentityf(&m[0][0]);
	m[0][0] = side[0];
	m[1][0] = side[1];
	m[2][0] = side[2];

	m[0][1] = up[0];
	m[1][1] = up[1];
	m[2][1] = up[2];

	m[0][2] = -forward[0];
	m[1][2] = -forward[1];
	m[2][2] = -forward[2];

	glMultMatrixf(&m[0][0]);
	glTranslatef(-eyex, -eyey, -eyez);
}


/* Call to render the next GL frame */
static void nativeRender(
		JNIEnv*  env,
		jobject thiz,
		jfloat cameraX,
		jfloat cameraY,
		jfloat cameraZ,
		jfloat lookAtX,
		jfloat lookAtY,
		jfloat lookAtZ,
		jboolean useTexture,
		jboolean useColor,
		jboolean cameraDirty) {
	//LOGI("render");

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    if (cameraDirty) {
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		gluLookAt(cameraX, cameraY, cameraZ,
				lookAtX, lookAtY, lookAtZ,
				0.0f, 1.0f, 0.0f);
    }
	glEnableClientState(GL_VERTEX_ARRAY);

	if (useTexture) {
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnable(GL_TEXTURE_2D);
	} else {
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisable(GL_TEXTURE_2D);
	}

	if (useColor) {
		glEnableClientState(GL_COLOR_ARRAY);
	} else {
		glDisableClientState(GL_COLOR_ARRAY);
	}

	if (sSkybox != -1) {
		glDepthMask(false);
		glDisable(GL_DEPTH_TEST);
		drawTile(sSkybox, cameraX, cameraY, cameraZ, useTexture, useColor);
		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
	}

	for (int x = 0; x < sTileCount; x++) {
		if (x != sSkybox) {
			drawTile(x, cameraX, cameraY, cameraZ, useTexture, useColor);
		}
	}

    glDisableClientState(GL_VERTEX_ARRAY);

    //LOGI("render complete");


}

static const char* classPathName = "com/android/heightmapprofiler/NativeRenderer";

static JNINativeMethod methods[] = {
  {"nativeReset", "()V", (void*)nativeReset },
  {"nativeRender", "(FFFFFFZZZ)V", (void*)nativeRender },
  {"nativeAddTile", "(IIFFFFFFF)I", (void*)nativeAddTile },
  {"nativeAddLod", "(IIIIIIZ)V", (void*)nativeAddLod },
  {"nativeSetSkybox", "(I)V", (void*)nativeSetSkybox },
  {"nativeAddTextureLod", "(III)V", (void*)nativeAddTextureLod},
};

/*
 * Register several native methods for one class.
 */
static int registerNativeMethods(JNIEnv* env, const char* className,
    JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;

    clazz = env->FindClass(className);
    if (clazz == NULL) {
    	char error[255];
        sprintf(error,
            "Native registration unable to find class '%s'\n", className);
        __android_log_print(ANDROID_LOG_ERROR, "HeightMapProfiler", error);
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
    	char error[255];
    	sprintf(error, "RegisterNatives failed for '%s'\n", className);
        __android_log_print(ANDROID_LOG_ERROR, "HeightMapProfiler", error);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

/*
 * Register native methods for all classes we know about.
 */
static int registerNatives(JNIEnv* env)
{
  if (!registerNativeMethods(env, classPathName,
                             methods, sizeof(methods) / sizeof(methods[0]))) {
    return JNI_FALSE;
  }

  return JNI_TRUE;
}

/*
 * Set some test stuff up.
 *
 * Returns the JNI version on success, -1 on failure.
 */
JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env = NULL;
    jint result = -1;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
    	LOGE("ERROR: GetEnv failed");
        goto bail;
    }
    assert(env != NULL);

    if (!registerNatives(env)) {
    	LOGE("ERROR: HeightMapProfiler native registration failed");
        goto bail;
    }

    /* success -- return valid version number */
    result = JNI_VERSION_1_4;

bail:
    return result;
}

