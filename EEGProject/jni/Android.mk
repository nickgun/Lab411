LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := ReadData
LOCAL_SRC_FILES := ./ReadData/edf2ascii.c matrix.c
include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)
LOCAL_MODULE    := Wavelet
LOCAL_SRC_FILES := ./Wavelet/wavelet_daub4.c
include $(BUILD_SHARED_LIBRARY)