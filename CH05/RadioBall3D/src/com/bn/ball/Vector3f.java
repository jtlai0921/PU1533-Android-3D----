package com.bn.ball;
/*
 * 代表3D空間中向量的類別
 */
public class Vector3f {
	//向量的三個分量
	float x, y, z;
	//建構器
	public Vector3f(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	//將向量規格化的方法
	public void normalize(){
		float mod = module();
		//確保模不為零時再規格化向量
		if(mod != 0){
			x = x/mod;
			y = y/mod;
			z = z/mod;
		}
	}
	//求向量的模的方法
	public float module(){
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
}
