LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := heightmapprofiler

LOCAL_CFLAGS := -DANDROID_NDK #-DREPORTING #-DDEBUG 

LOCAL_SRC_FILES := \
    height_map.cpp \

LOCAL_LDLIBS := -lGLESv1_CM -ldl -llog

include $(BUILD_SHARED_LIBRARY)
