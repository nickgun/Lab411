#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <math.h>
#include "eegdata_conf.h"
#include "matrix.h"
#include "fastICA.h"
#include "edf2ascii.h"
#include "wavelet_daub4.h"

char *pPath;
char *pFile; 


/**
 * Ham tinh Cross Correlation
 */
double calcCrossCorrelation(double* DataX, double* DataY, int Sample){
  double DenomX=0; 
  double DenomY=0;
  double MeanX=0;
  double MeanY=0;
  double CrossCorr=0;
  int i;
  
  for(i=0; i<Sample; i++){
    MeanX+=DataX[i];
    MeanY+=DataY[i];
  }
  MeanX=MeanX/Sample;
  MeanY=MeanY/Sample;
  
  for(i=0; i<Sample; i++){
    CrossCorr+=((DataX[i]-MeanX)*(DataY[i]-MeanY));
    DenomX+=pow((DataX[i]-MeanX),2);
    DenomY+=pow((DataY[i]-MeanY),2);
  }
  
  CrossCorr=CrossCorr/sqrt(DenomX*DenomY);
  printf("CrossCorr = %f", CrossCorr);
  
  return CrossCorr;
}

//void removeEyeBlink(char *pFilePath, int StartSample, int StopSample){
void main(){
  int i, j, k;
  int Level = 2;
  char *pFilePath = "/home/nick_gun/Desktop/RemoveEyeBlink/Khoalan2.edf";
  int StartSample = 16300;
  int StopSample = 16556;
  
  int Sample = 256;
  int Rows = 256;
  int Cols = 19;
  int Comp = 19;
  
  

  //chi xu ly tung doan 256 mau
  
  double** RowData;
  double** DataICA, **K, **W;
  double** DataICATranspose;
  double** DataICA_IDWT;
  double*  DataFp2;
  double*  DataFp2_IDWT;
  double** DataRecontruction;
  
  double CrossCorrMax=0;
  double CrossCorr=0;
  int IndexCompEyeblink=0;
  
  
  RowData = mat_create(Rows, Cols);

  /**
   *
   * B0: Detect thoi dien liec mat
   * phan nay tam thoi chua cho vao
   *
   */
  for(int i=0; i<Sample; i++){
     //Kenh F8: dich di tung doan 50 mau
     //tinh gia tri trung binh(GTTB) cua 50 mau
     //tinh sai khac GTTB so voi 50 mau truoc day
     //neu vuot nguong thi tinh gia tri nay tren kenh F7
     //xac dinh duoc thoi diem eyeblink
  }
  
  /**
   * B1: Tinh toan ICA va Wavelet
   * Du lieu chia lam 2 luong
   */
  /**
   * Luong 1: Doc du lieu cua 19 kenh, chay ICA
   */
  //b1: doc du lieu: 256 Sample cua 19 kenh, luu vao mang RowData
  readData(pFilePath, RowData, StartSample, StartSample+Sample);
  
  //b2: chay ICA voi RowData
  DataICA = mat_create(Rows, Comp);
  W = mat_create(Comp, Comp);	//
  K = mat_create(Cols, Comp);	//
  icaTransform(RowData, Rows, Cols, Comp, DataICA, K, W);
  //b3: chay Wavelet Inverse bac 3 voi tung thanh phan doc lap
  DataICATranspose = mat_create(Comp, Rows);
  DataICA_IDWT = mat_create(Comp, Rows);
  mat_transpose(DataICA, Rows, Comp, DataICATranspose);
  for(i=0; i<Comp; i++){
    DataICA_IDWT[i]=WaveletTransformInverse(DataICATranspose[i], Sample, i, Level);
  }
  
  /**
   * Luong 2: Xu ly du lieu tren kenh FP2
   */
  //b1: lay du lieu kenh Fp2
  DataFp2 = malloc(Sample*sizeof(double));
  for(i=0; i<Sample; i++)
    DataFp2[i] = RowData[i][13];
  
  //b2: chay Wavelet Inverse bac 3 voi DataFp2
  DataFp2_IDWT = malloc(Sample*sizeof(double));
  DataFp2_IDWT=WaveletTransformInverse(DataFp2, Sample, CHANNEL_NUMBER_MAX, Level);
  
  printf("--- %f \n", DataFp2_IDWT[255]);
  /**
   * B2: Tinh Cross Correlator
   * Xac dinh thanh phan nhieu mat
   */
  for(k=0; k<Comp; k++){
    printf("\nComp %i: ", k);
    CrossCorr=calcCrossCorrelation(DataFp2_IDWT, DataICA_IDWT[k], Sample);
    if(CrossCorrMax<CrossCorr){
      CrossCorrMax=CrossCorr;
      IndexCompEyeblink=k;
    }
  }
  
  //hien thi thanh phan nhieu mat trong cac thanh phan doc lap
  printf("\nComponent Eyeblink: %i, CrossCorr: %f\n", IndexCompEyeblink, CrossCorrMax);
  
  /**
   * B3: Loai bo nhieu mat, tai cau truc du lieu - dung ICA nguoc
   */
  DataRecontruction = mat_create(Rows, Cols);
  icaTransformInverse(DataICA, Rows, Cols, Comp, IndexCompEyeblink, DataRecontruction, K, W, RowData);
  mat_delete(RowData, StopSample-StartSample, CHANNEL_NUMBER_MAX);
  mat_delete(DataICA, Rows, Comp);
  mat_delete(DataICATranspose, Comp, Rows);
  mat_delete(DataICA_IDWT, Comp, Rows);
  mat_delete(K, Cols, Comp);
  mat_delete(W, Comp, Comp);
  mat_delete(DataRecontruction, Rows, Cols);
  free(DataFp2);
  free(DataFp2_IDWT);
}
