package com.bn.gjxq.game;

 
//���������R�A���
public class GameStaticData 
{
    //��l���¥թ�  
	//0---��
	//1---��
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
	
	//��l��X\Z�����q
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
	
	//�Ѥl��X\Z�����q
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
	
	//�¥մѤl�����ਤ��     0---��  1---��
	static float[] ANGLE={0,-180};
}
