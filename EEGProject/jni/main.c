#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <locale.h>
#include "edf2ascii.h"
#include "matrix.h"
#include "eegdata_conf.h"
#include "fastICA.h"
#include <jni.h>

#define DEBUG
#define SAMPLE 128

/*
 * thuc hien recontruction de tai tao lai tin hieu
 */
double *reWavelet(int _SAMPLE, int LFre, int HFre, double *_Data){
  double *_Data_DWT;
  double *_Data_iDWT;
  int i;
  
  _Data_DWT = malloc(_SAMPLE*sizeof(double));
  _Data_iDWT = malloc(_SAMPLE*sizeof(double));
  
  //bien doi wavelet
  _Data_DWT = daub4_transform(_SAMPLE, _Data);
  
  //recontruction wavelet
  for(i=0;i<LFre;i++)
    _Data_DWT[i] = 0;
  for(i=HFre;i<_SAMPLE;i++)
    _Data_DWT[i] = 0;
  
  _Data_iDWT = daub4_transform_inverse(_SAMPLE, _Data_DWT);
  
  free(_Data_DWT);
  return _Data_iDWT;
}

int ProcessData(){
  int i, j, ii;
  int comp = 14; //so thanh phan doc lap
  
  //khai bao matrix X la ma tran dung de luu eegData doc tu file Edf
  mat X;
  int rows_X, cols_X;//so hang, cot cua ma tran
  
  mat K, W, A, S;
  
  double *Af3; //du lieu kenh AF3
  double *EyeBlink;//dang tin hieu eyeblink
  double *Component;
  double *Comp_Wavelet;
  double Mean_X, Mean_Y, S_X, S_Y, Denom, Cr_Correlate;
  
  FILE *FileOut;
  
  X = mat_create(SAMPLE_MAX, CHANNEL_NUMBER);//cap phat bo nho cho X
  
  /**
   * READ DATA
   */
  printf("\nStart read EEG data..........\n");
  rows_X = readData(X);//goi ham doc file edf, luu du lieu vao ma tran X
  cols_X = CHANNEL_NUMBER;
  rows_X = 128;
  //END READ DATA
  
  /**
   * RUN ICA
   */
  printf("\nStart run ICA..........\n");
  //cap phat vung nho cho cac matrix
  W = mat_create(comp, comp); 
  A = mat_create(comp, comp);
  K = mat_create(cols_X, comp);
  S = mat_create(rows_X, comp);	
  
  fastICA(X, rows_X, cols_X, comp, K, W, A, S);//run ICA, matrix output: S
#ifdef DEBUG
  //kiem tra xem matrix S thu ve co dung khong
  FileOut = fopen("/home/nick_gun/Desktop/Lab-2013/SAMSUNG_2013_2014/Code/EEGProject/Debug/ICA_Out.txt", "wb");
  for(i=0; i<rows_X; i++){
    for(j=0;j<1;j++)
      fprintf(FileOut, "%f ", S[i][3]);
    fprintf(FileOut, "\n");  
  }
  fclose(FileOut);
#endif
  
  //END RUN ICA
  
  
  /**
   * RUN WAVELET
   * Compute Wavelet and Recontruction Detail/Aprroximation 
   * with AF3(for recontruction eyeblink) and each component(ICA)
   * Then use Cross Correlation for Detect EyeBlink Component
   * 
   * Ref: http://paulbourke.net/miscellaneous/correlate/
   */
  printf("\nStart run Wavelet..........\n");
  //1.RUN WAVELET FOR FA3
  //copy du lieu sang Af3
  Af3 = malloc(SAMPLE*sizeof(double));
  for(i=0;i<SAMPLE;i++)
    Af3[i]=X[i][5];
  
  //chay wavelet daub4 cho Af3 va tai tao tin hieu eyeblink
  EyeBlink = reWavelet(SAMPLE, 32, 64, Af3);
  
  //compute mean of EyeBlink and absolute EyeBlink
  S_X=0; Mean_X=0;
  for(i=0;i<SAMPLE;i++){
    //if(EyeBlink[i]<0) EyeBlink[i]=-EyeBlink[i];
    Mean_X+=EyeBlink[i];
  }
  Mean_X=Mean_X/SAMPLE;
  for(i=0;i<SAMPLE;i++)
    S_X+=(EyeBlink[i]-Mean_X)*(EyeBlink[i]-Mean_X);
    
#ifdef DEBUG
  //print eyeblink
  FileOut = fopen("/home/nick_gun/Desktop/Lab-2013/SAMSUNG_2013_2014/Code/EEGProject/Debug/Eyeblink.txt", "wb");
  for(i=0; i<SAMPLE; i++)
    fprintf(FileOut, "%f\n", EyeBlink[i]);
  fclose(FileOut);
#endif  
  
  //2.RUN WAVELET FOR COMPONENT ICA
  Comp_Wavelet = malloc(SAMPLE*sizeof(double));
  Component = malloc(SAMPLE*sizeof(double));
  
  
  
  //loop load one of all channel
  for(ii=0;ii<comp;ii++){
    //2.1.COMPUTE WAVELET FOR EACH COMPONENT
    for(j=0;j<SAMPLE;j++)
      Component[j]=S[j][ii];
    Comp_Wavelet = reWavelet(SAMPLE, 16, 32, Component);
    
#ifdef DEBUG
    if(ii==0)	FileOut = fopen("/home/nick_gun/Desktop/Lab-2013/SAMSUNG_2013_2014/Code/EEGProject/Debug/Comp_Wavelet.txt", "wb");
    else 	FileOut = fopen("/home/nick_gun/Desktop/Lab-2013/SAMSUNG_2013_2014/Code/EEGProject/Debug/Comp_Wavelet.txt", "at");
    fprintf(FileOut, "Comp_Wavelet %d ***************\n", ii);
    for(j=0; j<SAMPLE; j++){
      //if(Comp_Wavelet[j]<0) Comp_Wavelet[j]=-Comp_Wavelet[j];
      fprintf(FileOut, "%f\n", Comp_Wavelet[j]); 
    }
    fclose(FileOut);
#endif
  
    //2.2.COMPUTE WITH EYEBLINK USE CROSS CORRELATION
    //compute mean and absolute Component
    S_Y=0; Mean_Y=0;
    for(j=0;j<SAMPLE;j++){
      //if(Comp_Wavelet[j]<0)	Comp_Wavelet[j]=-Comp_Wavelet[j];
      Mean_Y+=Comp_Wavelet[j];
    }
    Mean_Y=Mean_Y/SAMPLE;
    for(j=0;j<SAMPLE;j++)
      S_Y+=(Comp_Wavelet[j]-Mean_Y)*(Comp_Wavelet[j]-Mean_Y);
    
    Denom = sqrt(S_X*S_Y);
    //compute cross correlate at delay = 0
    Cr_Correlate=0;
    for(j=0;j<SAMPLE;j++){
      Cr_Correlate+=EyeBlink[j]*Comp_Wavelet[j];
    }
    Cr_Correlate=Cr_Correlate/Denom;
    printf("==%d===%f===\n",ii,  Cr_Correlate);
  }
  //END RUN WAVELET
  
  free(Af3);
  free(EyeBlink);
  free(Component);
  free(Comp_Wavelet);
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
