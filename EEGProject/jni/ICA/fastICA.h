/**
 * @file libICA.h
 * 
 * Main FastICA functions.
 */

#ifndef LIBICA_H_
#define LIBICA_H_

#define MAX_ITERATIONS	5000
#define TOLERANCE		0.0001
#define ALPHA			1

void fastICA(double** X, int rows, int cols, int compc, double** K, double** W, double** A, double** S);
void icaTransform(char *pFilePath, int rows, int cols,int comp);

#endif /*LIBICA_H_*/
