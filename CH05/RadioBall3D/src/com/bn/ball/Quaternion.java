package com.bn.ball;
/*
 * �N��|���ƪ����O
 */
public class Quaternion {
	//�|���ƪ��|�Ӥ��q��
	float w, x, y, z;
	//���|����
	public static Quaternion getIdentityQuaternion()
	{
		return  new Quaternion(1f, 0f, 0f, 0f);
	}

	public Quaternion() {	}
	public Quaternion(float w, float x, float y, float z) 
	{
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	//�غc¶���w�b���઺�|���ƪ���k
	public void setToRotateAboutAxis(Vector3f axis, float theta){
		//����b�����W���
		axis.normalize();
		//�p��b���Msin��
		float thetaOver2 = theta/2f;
		float sinThetaOver2 = (float) Math.sin(thetaOver2);
		//������
		w = (float) Math.cos(thetaOver2);
		x = axis.x * sinThetaOver2;
		y = axis.y * sinThetaOver2;
		z = axis.z * sinThetaOver2;
	}
	//���R���ਤ����k
	public float getRotationAngle(){
		//�p��b���Aw = cos(theta/2)
		float thetaOver2 = (float) Math.acos(w);
		//�Ǧ^���ਤ
		return thetaOver2 * 2f;
	}
	//���R����b����k
	public Vector3f getRotationAxis(){
		//�p��sin^2(theta/2), �O��w = cos(theta/2),  sin^2(x) + cos^2(x) = 1
		float sinThetaOver2Sq = 1.0f - w*w;
		//�`�N�T�O�ƭȺ��
		if(sinThetaOver2Sq <= 0.0f){
			//���|���ƩΤ���T���ƭȡA�u�n�Ǧ^���Ī��V�q�Y�i
			return new Vector3f(1.0f, 0f, 0f);
		}
		//�p��1/sin(theta/2)
		float oneOversinThetaOver2 = (float) (1.0f/Math.sqrt(sinThetaOver2Sq));
		//�Ǧ^����b
		return new Vector3f(
				x * oneOversinThetaOver2,
				y * oneOversinThetaOver2,
				z * oneOversinThetaOver2
				);
	}
	/*
	 * �|���Ƥe���B��A�ΥH���`�h������A
	 * �������ǬO�q���V�k�A
	 * �o�M�|���Ƥe�������Э㡨�w�q�ۤ�
	 */
	public Quaternion cross(Quaternion a){
		Quaternion result = new Quaternion();
		
		result.w = w*a.w - x*a.x - y*a.y - z*a.z;
		result.x = w*a.x + x*a.w + z*a.y - y*a.z;
		result.y = w*a.y + y*a.w + x*a.z - z*a.x;
		result.z = w*a.z + z*a.w + y*a.x - x*a.y;
		
		return result;
	}
}
