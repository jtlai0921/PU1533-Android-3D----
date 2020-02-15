package com.bn.cube.game;
public class ReflectUtil {

	public  Vector3 vecI;
	public  Vector3 vecN;
	public ReflectUtil()
	{
		
	}
	public ReflectUtil(Vector3 vec1,Vector3 vec2)
	{
		this.vecI=vec1;
		this.vecN=vec2;
	}

	public void set(Vector3 vec1,Vector3 vec2)
	{
		this.vecI=Vector3.normalize(vec1);
		this.vecN=Vector3.normalize(vec2);
	}
	
	public  Vector3 getReflect()
	{
		Vector3 vec=new Vector3();
		vec=Vector3.sCheng(2.0f*Vector3.dot(vecI, vecN),vecN);
		vec=Vector3.jian(vecI, vec);
		return vec;
	}
	
}
