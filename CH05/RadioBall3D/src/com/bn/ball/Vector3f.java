package com.bn.ball;
/*
 * �N��3D�Ŷ����V�q�����O
 */
public class Vector3f {
	//�V�q���T�Ӥ��q
	float x, y, z;
	//�غc��
	public Vector3f(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	//�N�V�q�W��ƪ���k
	public void normalize(){
		float mod = module();
		//�T�O�Ҥ����s�ɦA�W��ƦV�q
		if(mod != 0){
			x = x/mod;
			y = y/mod;
			z = z/mod;
		}
	}
	//�D�V�q���Ҫ���k
	public float module(){
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
}
