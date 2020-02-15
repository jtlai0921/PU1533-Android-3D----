package com.bn.GL20Tail;

//向量計算方法的封裝類別
public class VectorUtil 
{
	//求兩個向量的叉積
	public static float[] getCrossProduct(float x1,float y1,float z1,float x2,float y2,float z2)
	{		
		//求出兩個向量叉積向量在XYZ軸的分量ABC
        float A=y1*z2-y2*z1;
        float B=z1*x2-z2*x1;
        float C=x1*y2-x2*y1;
		
		return new float[]{A,B,C};
	}
	
	//向量規格化
	public static float[] vectorNormal(float[] vector)
	{
		//求向量的模
		float module=(float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
		return new float[]{vector[0]/module,vector[1]/module,vector[2]/module};
	}
	
	//求兩個向量的點積
	public static float dotProduct(float[] vec1,float[] vec2)
	{
		return
			vec1[0]*vec2[0]+
			vec1[1]*vec2[1]+
			vec1[2]*vec2[2];
		
	}   
	
	//求向量的模
	public static float mould(float[] vec)
	{
		return (float)Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
	}
	
	//求兩個向量的夾角
	public static float angle(float[] vec1,float[] vec2)
	{
		//先求點積
		float dp=dotProduct(vec1,vec2);
		//再求兩個向量的模
		float m1=mould(vec1);
		float m2=mould(vec2);
		
		float acos=dp/(m1*m2);
		
		//為了避免計算誤差帶來的問題
		if(acos>1)
		{
			acos=1;
		}
		else if(acos<-1)
		{
			acos=-1;
		}
			
		return (float)Math.acos(acos);
	}
	
	
}
