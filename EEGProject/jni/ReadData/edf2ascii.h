

//typedef double **mat;
int readAllData(char *pFilePath);
int readAllDataOfChannel(char *pFilePath, int Channel);
int readDataOfChannel(char *pFilePath, int Start_Sample, int Stop_Sample, int Channel);
int readData(char *pFilePath, int Start_Sample, int Stop_Sample, int Channel);
