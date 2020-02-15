package com.bn.gjxq.manager;

//用於儲存3D笛卡爾座標系中點或向量的類別
public class Vector3f
{
	float x;
	float y;
	float z;
	public Vector3f(float x,float y,float z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	//減法
	public Vector3f minus(Vector3f v){
		return new Vector3f(this.x-v.x,this.y-v.y,this.z-v.z);
	}
	
	//求兩點距離的平方
	public static float disP2(Vector3f v1,Vector3f v2)
	{
		return (v1.x-v2.x)*(v1.x-v2.x)+(v1.y-v2.y)*(v1.y-v2.y)+(v1.z-v2.z)*(v1.z-v2.z);
	}
}
