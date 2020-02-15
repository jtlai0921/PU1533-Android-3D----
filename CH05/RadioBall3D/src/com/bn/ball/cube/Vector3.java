package com.bn.ball.cube;
public class Vector3 {

	float x;
	float y;
	float z;
	private static Vector3 vec;
	public Vector3(){};
	
	public Vector3(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public static void set(float x,float y,float z)
	{
		vec.x=x;
		vec.y=y;
		vec.z=z;
	}
	public static  Vector3 get()
	{
		return vec;
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
	//求向量件夾角
		public static float getAngle(Vector3 vec1,Vector3 vec2)
		{
			float angle=0;
	        vec1=normalize(vec1);
	        vec2=normalize(vec2);
	        angle=(float) Math.acos(dot(vec1,vec2));;
	        angle=(float) (angle/3.14*180);
			return angle;
		}
		
}
