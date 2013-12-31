#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "DetectGazeEye.h"
#include <jni.h>



static double *read_data_from_file(char *pFilePath, int Sample)
  {
    int i; 
    double *u;
    FILE *fl;
    char Path[512];
    
    strcpy(Path, pFilePath);
   
    fl = fopen(Path, "r");
    u = malloc ( Sample * sizeof ( double ) );
    
    for (i=0; i<Sample; i++) {
	fscanf(fl, "%lf ", &(u[i]));	
    }
    fclose(fl);
    return u;	
  }
  
void detectGazeEye(char *pF8Path, char *pF7Path, int Sample){
  char pFilePath[512];
  double * DataF8;
  double * DataF7;
  int i, j;
  int DeltaF8, DeltaF7;
  int ir =0;	//so lan liec phai
  int il = 0;	//so lan liec trai
  int ieb = 0;	//so lan nhay mat
  FILE *OutputFile;
  
  State = GazeCenter;
  pGazeRight = malloc(100*sizeof(int));
  pGazeLeft = malloc(100*sizeof(int));
  pEyeBlink = malloc(100*sizeof(int));
  
  DataF8 = read_data_from_file(pF8Path, Sample);
  DataF7 = read_data_from_file(pF7Path, Sample);
  
  for(i=0; i<Sample; i+=4){
    j=i+32;
    if(j>Sample)
      break;
    
    DeltaF8 = DataF8[j] - DataF8[i];
    
    
    if(DeltaF8>Threshold){	//do thi dot bien theo huong len
      if(DeltaF7>Threshold){	//detect eyeblink
	pEyeBlink[ieb]=i;
	ieb++;
      }else{			//detect gaze eye
	if(State==GazeCenter){	
	  printf("\nDectect Start GazeRight: %i Sample", i);
	  State=GazeRight;
	  pGazeRight[ir]=i;	//luu thoi diem phat hien dot bien len vao mang
	  ir++;
	}else if(State==GazeLeft){	//neu GazeState == GazeLeft -> ung voi TH dang o trang thai liec trai-> tro ve trang thai ban dau
	  printf("\nEnd GazeLeft %i Sample", i);
	  State = GazeCenter;
	  pGazeLeft[il] = i;	//luu thoi diem phat hien dot bien len
	  il++;
	}
      }
      i+=32;
    }else if(DeltaF8<-Threshold){	//do thi dot bien theo huong xuong
      if(State==GazeCenter){	
	printf("\nDectect Start GazeLeft %i Sample", i);
	State=GazeLeft;
	pGazeLeft[il]=i;	//luu thoi diem phat hien dot bien len vao mang
	il++;
      }else if(State==GazeRight){	//neu GazeState == GazeRight -> ung voi TH dang o trang thai liec phai-> tro ve trang thai ban dau
	printf("\nEnd GazeRight %i Sample", i);
	State = GazeCenter;
	pGazeRight[ir] = i;	//luu thoi diem phat hien dot bien len
	ir++;
      }
      i+=32;
    }
  }
  
  strcpy(pFilePath, pF8Path);
  strcat(pFilePath, "_DetectEye");
  OutputFile = fopen(pFilePath, "wb");
  fprintf(OutputFile, "%i\n%i\n%i\n", ieb, ir, il);
  for(i=0; i<ieb; i++)
    fprintf(OutputFile, "%i\n", pEyeBlink[ieb]);
  for(i=0; i<ir; i++)
    fprintf(OutputFile, "%i\n", pGazeRight[i]);
  for(i=0; i<il; i++)
    fprintf(OutputFile, "%i\n", pGazeLeft[i]);
  
  fclose(OutputFile);
  
  free(pGazeLeft);
  free(pGazeRight);
  free(pEyeBlink);
}


JNIEXPORT jint JNICALL
Java_com_eegsdk_SdkMain_detectEye(JNIEnv *env, jobject thisObj, jstring javaString1, jstring javaString2, jint Sample){
   const char *nativeString1 = (*env)->GetStringUTFChars(env, javaString1, 0);
   const char *nativeString2 = (*env)->GetStringUTFChars(env, javaString2, 0);
   // use your string
   detectGazeEye(nativeString1, nativeString2, Sample);
   
   (*env)->ReleaseStringUTFChars(env, javaString1, nativeString1);
   (*env)->ReleaseStringUTFChars(env, javaString2, nativeString2);
  return 1;
}