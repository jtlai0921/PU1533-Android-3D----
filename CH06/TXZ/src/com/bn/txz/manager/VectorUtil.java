package com.bn.txz.manager;

//�V�q���p�⪺�u�����O
public class VectorUtil 
{
	//�D��ӦV�q���e�n
	public static float[] getCrossProduct(float x1,float y1,float z1,float x2,float y2,float z2)
	{		
		//�D�X��ӦV�q�e�n�V�q�bXYZ�b�����qABC
        float A=y1*z2-y2*z1;
        float B=z1*x2-z2*x1;
        float C=x1*y2-x2*y1;
		
		return new float[]{A,B,C};
	}
	
	//�V�q�W���
	public static float[] vectorNormal(float[] vector)
	{
		//�D�V�q����
		float module=(float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
		return new float[]{vector[0]/module,vector[1]/module,vector[2]/module};
	}
}
