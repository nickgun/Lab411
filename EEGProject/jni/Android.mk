#Copyright (C) 2013 The Android Open Source Project

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := LoadDataEEG
LOCAL_SRC_FILES := LoadDataEEG.c

include $(BUILD_SHARED_LIBRARY)