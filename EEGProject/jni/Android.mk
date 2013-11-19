LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := ProcessData
LOCAL_SRC_FILES := main.c edf2ascii.c fastICA.c matrix.c svdcmp.c
include $(BUILD_SHARED_LIBRARY)