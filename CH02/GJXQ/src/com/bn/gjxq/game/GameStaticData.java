package com.bn.gjxq.game;

 
//游戲中的靜態資料
public class GameStaticData 
{
    //格子的黑白性  
	//0---白
	//1---黑
	static int[][] gzhb=
	{
		{0,1,0,1,0,1,0,1},
		{1,0,1,0,1,0,1,0},
		{0,1,0,1,0,1,0,1},
		{1,0,1,0,1,0,1,0},
		{0,1,0,1,0,1,0,1},
		{1,0,1,0,1,0,1,0},
		{0,1,0,1,0,1,0,1},
		{1,0,1,0,1,0,1,0}
	};
	
	//格子的X\Z偏移量
	static float[][] XOffset=new float[8][8];
	static float[][] ZOffset=new float[8][8];	
	static 
	{
		float UNIT_SIZE=1.2f;
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				XOffset[i][j]=(j-4) * UNIT_SIZE+0.5f*UNIT_SIZE;
				ZOffset[i][j]=(i-4) * UNIT_SIZE+0.5f*UNIT_SIZE;
			}
		}
	}
	
	//棋子的X\Z偏移量
	static float[][] XOffsetQZ=new float[8][8];
	static float[][] ZOffsetQZ=new float[8][8];	
	static 
	{
		float UNIT_SIZE_QZ=1.2f;
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				XOffsetQZ[i][j]=(j-4)*UNIT_SIZE_QZ+0.5f*UNIT_SIZE_QZ;
				ZOffsetQZ[i][j]=(i-4)*UNIT_SIZE_QZ+0.5f*UNIT_SIZE_QZ;
			}
		}
	}
	
	//黑白棋子的旋轉角度     0---白  1---黑
	static float[] ANGLE={0,-180};
}
