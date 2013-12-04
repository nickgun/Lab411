LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := ProcessData
LOCAL_SRC_FILES := main.c ./ReadData/edf2ascii.c ./ICAfastICA.c matrix.c ./ICA/svdcmp.c
include $(BUILD_SHARED_LIBRARY)