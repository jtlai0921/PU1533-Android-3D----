package com.f.util;

import static com.f.pingpong.Constant.*;

import javax.vecmath.Vector3f;

public class CalculateUtil 
{
	//�p��ﭱ���I����m  �M �ϼu�t��
	public static Vector3f[] getPosAndSpeB(Vector3f position,Vector3f speed)
	{
		Vector3f[] result = new Vector3f[2];
		Vector3f positionB = null;
		Vector3f speedB = null;
		//y��V�W �N�O�ݪ��W��  �_�l�t��speed.y  �_�l���׬O  �y�x�b��+�b�|
		
		//��̰��I���ɶ�
		float t1 = Math.abs(speed.y/A.y);
		//��̰��I���Z��
		float s1 = Math.abs(speed.y*speed.y/(2*A.y));
		//�̰��I��ୱ���Z��
		float s2 = s1+position.y-(TABLE_Y + BALL_R);
		//�̰��I��ୱ���ɶ�
		float t2 = (float) Math.sqrt(Math.abs(2*s2/A.y));
		//�y��ﭱ��l�W(B�I)���ɶ�
		float t = t1+t2;
		//B�I x z�y��
		float px = position.x+speed.x*t;
		float pz = position.z+speed.z*t;
		positionB = new Vector3f(px,TABLE_Y + BALL_R,pz);
		
		//�p��ϼu��@�������t��  x z�W���[�t�׼Ȯɬ� 0 
		float vx = speed.x + A.x * t;
		float vy = Math.abs((speed.y + A.y * t)*BALL_ENERGY_LOST);
		float vz = speed.z + A.z * t;
		speedB = new Vector3f(vx,vy,vz);
		
		result[0]=positionB;
		result[1]=speedB;
		return result;
	}
	
	//�ھڲy�b�ﭱ���I��m�M�t��    �p�⼲�I��m�M�I���e�t��
	public static Vector3f[] getPosAndSpeC(Vector3f positionB,Vector3f speedB)
	{
		Vector3f[] result = new Vector3f[2];
		Vector3f positionC = null;
		Vector3f speedC = null;
		
		//���I �P �ﭱ���I��z�b�W���Z��
		float s = Math.abs(AI_Z-positionB.z);//�Ȯɳ]�w�y�x�̻��䬰    �I����
		//���I�켲�I���ɶ�
		float t = Math.abs(s/speedB.z);
		
		float px = positionB.x+speedB.x*t;
		float py = positionB.y+(speedB.y*t+0.5f*A.y*t*t);//�o�Ӧa�� �j����0.01���~�t  �ʤ���1��2
		float pz = AI_Z;
		positionC = new Vector3f(px,py,pz);
		
		float vx = speedB.x;
		float vy = speedB.y+A.y*t;
		float vz = speedB.z;
		speedC = new Vector3f(vx,vy,vz);

		result[0] = positionC;
		result[1] = speedC;
		return result;
	}
	
	//�b�����I�d�� �H�����ͤ@�ӥت������I    �H�����ϰ�ȩw���x�� ratio���@�Ӥ��  �ȶV�j �x�νd��V�j
	//TABLE_X_MIN<center.x<TABLE_X_MAX
	//TABLE_Z_MAX/3<center.z<TABLE_Z_MAX
	public static Vector3f randomTarget(Vector3f center,float ratio)
	{
		Vector3f result = new Vector3f();
		float maxl = TABLE_WIDHT * ratio;
		float maxw = TABLE_LENGTH * ratio / 3.0f;
		float x1 = (float) (maxl * (Math.random()-0.5f));
		float z1 = (float) (maxw * (Math.random()-0.5f));
		result.x = center.x + x1;
		result.y = center.y ;
		result.z = center.z + z1;
		return result;
	}
//	//�ھڸ��I�p��g�X�t�� vy < 0
//	public static Vector3f getOutSpeedDown0(Vector3f target,Vector3f position_out,float vy)
//	{
//		//s = (vt*vt - v0*v0) / 2*g
//		float s  = position_out.y - TABLE_Y;
//		float vt = (float) Math.sqrt(2 * s * A.y + vy * vy);
//		float t  = Math.abs((vt - vy) / A.y);
//		
//		float vx = (target.x - position_out.x )/ t;
//		float vz = (target.z - position_out.z )/ t;
//		
//		return new Vector3f(vx,vy,vz);
//	}
	//�ھڸ��I�p��g�X�t�� vy > 0
	public static Vector3f getOutSpeedUp(Vector3f target,Vector3f position_out,float vy)
	{
		Vector3f result = new Vector3f();
		//�W�߮ɶ�
		float t1 = Math.abs(vy / A.y);
		//�W�߶Z��
		//float s1 = Math.abs(vy * vy / (2 * A.y));//v*v = 2 * g *h
		float s1 = vy * t1 + 0.5f * A.y * t1 *t1 ;//s = vt + 0.5f * g * t * t
		//�U���ɶ�
		float t2 = (float) Math.sqrt(Math.abs(2 * (s1 + position_out.y) / A.y));
		//�`�ɶ�
		float t = t1+t2;
		
		float vx = (target.x - position_out.x )/ t;
		float vz = (target.z - position_out.z )/ t;

		result.x = vx;
		result.y = vy;
		result.z = vz;
		
		return result;
	}
	
	//�V�qvec1�Pvec2���������u
	public static Vector3f getBisector(Vector3f vecIn,Vector3f vecOut)
	{
		Vector3f bisector = new Vector3f();
		Vector3f tempOut = new Vector3f(vecOut.x,vecOut.y,vecOut.z);
		Vector3f tempIn = new Vector3f(vecIn.x,vecIn.y,vecIn.z);
		tempOut.normalize();
		tempIn.normalize();
		tempIn.x = -tempIn.x;
		tempIn.y = -tempIn.y;
		tempIn.z = -tempIn.z;
		bisector.x = tempIn.x + tempOut.x;
		bisector.y = tempIn.y + tempOut.y;
		bisector.z = tempIn.z + tempOut.z;
		bisector.normalize();
		return bisector;
	}

	//�Px,y,z�b������,�Y�y������¶x,y,z���઺����
	public static Vector3f getAngle(Vector3f bisector){
		
		//�T�Ӯy�жb�V�q
		Vector3f xaxle = new Vector3f(1,0,0); 
		Vector3f yaxle = new Vector3f(0,1,0); 
		Vector3f zaxle = new Vector3f(0,0,1);
		//bisector���O�bx-o-y,y-o-z,z-o-x�����W����v
		Vector3f xoy = new Vector3f(bisector.x,bisector.y,0); 
		Vector3f yoz = new Vector3f(0,bisector.y,bisector.z); 
		Vector3f xoz = new Vector3f(bisector.x,0,bisector.z);
		xoy.normalize();
		yoz.normalize();
		xoz.normalize();
		//��v�P�y�жb������
		float xAngle = (float) Math.acos(yaxle.dot(yoz));
		float yAngle = (float) Math.acos(zaxle.dot(xoz));
		float zAngle = (float) Math.acos(xaxle.dot(xoy));
		return new Vector3f(xAngle, yAngle, zAngle);
	}

	//�p��y�O�_����	//positionC�w�����y�P���z�ƪ������I,target�W���{���H�������I
	public static boolean isCollNet(Vector3f position_out,Vector3f target,float vy)
	{
		//�W�߮ɶ�
		float t1 = Math.abs(vy / A.y);
		//�W�߶Z��
		//float s1 = Math.abs(vy * vy / (2 * A.y));//v*v = 2 * g *h
		float s1 = vy * t1 + 0.5f * A.y * t1 * t1 ;//s = vt + 0.5f * g * t * t
		//�U���ɶ�
		float t2 = (float) Math.sqrt(Math.abs(2 * (s1 + position_out.y) / A.y));
		//�`�ɶ�
		float t = t1+t2;
		//z��V�t��
		float vz = (target.z - position_out.z )/ t;
		//�q���y�I������ɶ�
		float tz = Math.abs( position_out.z / vz );
		//�btz�ɶ��yy��V���첾
		float s2 = vy * tz + 0.5f * A.y * tz * tz;
		//�btz�ɶ���y��y��
		float s3 = position_out.y + s2;
		if(s3 > NET_Y + BALL_R){
			return false;
		}else{
			return true;
		}
	}
}