

//typedef double **mat;
int readDataOfAllChannel(char *pFilePath, int Start_Sample, int Stop_Sample);
int readDataOfChannel(char *pFilePath, int Start_Sample, int Stop_Sample, int Channel);
int readAllDataOfChannel(char *pFilePath, int Channel);
int readAllDataOfAllChannel(char *pFilePath);

char ChanNum[19][3] = {
  "Pz", "Cz", "Fz", "P3", "C3", "F3", "Fp1", "T5", "T3", "F7", "P4", "C4", "F4", "Fp2", "T6", "T4", "F8", "O2", "O1"
};