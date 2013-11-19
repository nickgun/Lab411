#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <locale.h>
#include "edf2ascii.h"
#include "matrix.h"
#include "eegdata_conf.h"
#include "fastICA.h"
#include <jni.h>

int ProcessData(){
  int i, j;
  int comp = 12; //so thanh phan doc lap
  
  //khai bao matrix X la ma tran dung de luu eegData doc tu file Edf
  mat X;
  int rows_X, cols_X;//so hang, cot cua ma tran
  
  mat K, W, A, S;
  
  FILE *matrixOut;
  
  X = mat_create(SAMPLE_MAX, CHANNEL_NUMBER);//cap phat bo nho cho X
  
  
  rows_X = readData(X);//goi ham doc file edf, luu du lieu vao ma tran X
  
  cols_X = CHANNEL_NUMBER;
  rows_X = 128;//tam xet voi tin hieu 1s
  
  //cap phat vung nho cho cac matri
  W = mat_create(comp, comp); 
  A = mat_create(comp, comp);
  K = mat_create(cols_X, comp);
  S = mat_create(rows_X, comp);	
  
  
  //ICA:
  fastICA(X, rows_X, cols_X, comp, K, W, A, S);
  
  //kiem tra xem matrix X thu ve co dung khong
  matrixOut = fopen("/storage/sdcard0/NickGun/EEG/matrix_ICA_Out.txt", "wb");
  for(i=0; i<rows_X; i++){
    for(j=0;j<comp;j++)
      fprintf(matrixOut, "%f ", S[i][j]);
    fprintf(matrixOut, "\n");  
  }
  fclose(matrixOut);
  
 
  mat_delete(X, SAMPLE_MAX, CHANNEL_NUMBER);//giai phong ma tran X
  mat_delete(W, comp, comp);
  mat_delete(A, comp, comp);
  mat_delete(K, cols_X, comp);
  mat_delete(S, rows_X, comp);
  
  return rows_X;
}

JNIEXPORT jint JNICALL
Java_com_uselibicademo_MainActivity_runProcessData(JNIEnv *env, jobject thisObj){
  return ProcessData();
}
