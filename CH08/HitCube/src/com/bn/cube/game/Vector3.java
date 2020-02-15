package com.bn.cube.game;

public class Vector3 {

	float x;
	float y;
	float z;
	
	public Vector3(){};
	
	public Vector3(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	//點積
	public static float dot(Vector3 v1,Vector3 v2){
		
		return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
	}
	//向量規格化
	public static Vector3 normalize(Vector3 vec3)
	{
		float distance=(float) Math.sqrt(vec3.x*vec3.x+vec3.y*vec3.y+vec3.z*vec3.z);
		return new Vector3(vec3.x/distance,vec3.y/distance,vec3.z/distance);
	}
	public static  Vector3 sCheng(float x,Vector3 vec3)
	{
		Vector3 vec=new Vector3();
		vec.x=x*vec3.x;
		vec.y=x*vec3.y;
		vec.z=x*vec3.z;
		return vec;
		
	}
	public static  Vector3 jian(Vector3 vec1,Vector3 vec2)
	{
		Vector3 vec=new Vector3();
		vec.x=vec1.x-vec2.x;
		vec.y=vec1.y-vec2.y;
		vec.z=vec1.z-vec2.z;
		return vec;
		
	}
	public static  Vector3 jia(Vector3 vec1,Vector3 vec2)
	{
		Vector3 vec=new Vector3();
		vec.x=vec1.x+vec2.x;
		vec.y=vec1.y+vec2.y;
		vec.z=vec1.z+vec2.z;
		return vec;
		
	}
	
	
		
}
