package com.cw.game;
import android.opengl.Matrix;
//���骺AABB�]��
public class AABB3
{
	MyVector3f min;
	MyVector3f max;
	//�Űѫغc��
	AABB3(){
		min = new MyVector3f();
		max = new MyVector3f();
		empty();
	}
	//�ѼƬ����I�}�C���غc��  
	AABB3(float[] vertices){
		min = new MyVector3f();
		max = new MyVector3f();
		empty();
		//�N�Ҧ����I�[�J�]��
		for(int i=0; i<vertices.length; i+=3){
			this.add(vertices[i], vertices[i+1], vertices[i+2]);
		}
	}
	//�M�zAABB
	public void empty(){
		min.x = min.y = min.z = Float.POSITIVE_INFINITY;//�N�̤p�I�]���̤j��
		max.x = max.y = max.z = Float.NEGATIVE_INFINITY;//�N�̤j�I�]���̤p��
	}
	//�N��@�I�[�J��AABB���A�æb���n���ɭԩ���AABB�H�]�A�C���I
	public void add(MyVector3f p){
		if (p.x < min.x) { min.x = p.x; }
		if (p.x > max.x) { max.x = p.x; }
		if (p.y < min.y) { min.y = p.y; }
		if (p.y > max.y) { max.y = p.y; }
		if (p.z < min.z) { min.z = p.z; }
		if (p.z > max.z) { max.z = p.z; }
	}
	public void add(float x, float y, float z){
		if (x < min.x) { min.x = x; }
		if (x > max.x) { max.x = x; }
		if (y < min.y) { min.y = y; }
		if (y > max.y) { max.y = y; }
		if (z < min.z) { min.z = z; }
		if (z > max.z) { max.z = z; }
	}
	//���oAABB�Ҧ����I�y�Ъ���k
	public MyVector3f[] getAllCorners(){
		MyVector3f[] result = new MyVector3f[8];
		for(int i=0; i<8; i++){
			result[i] = getCorner(i);
		}
		return result;
	}
	//���oAABB��i�ӳ��I�y�Ъ���k
	public MyVector3f getCorner(int i){		
		if(i<0||i>7){//�ˬdi�O�_�X�k
			return null;
		}
		return new MyVector3f(
				((i & 1) == 0) ? max.x : min.x,
				((i & 2) == 0) ? max.y : min.y, 
				((i & 4) == 0) ? max.z : min.z
				);
	}
	//�z�L�ثe��g�ܴ��x�}�D�o��g�ܴ��᪺AABB�]�򲰪���k
	public AABB3 setToTransformedBox(float[] m)
	{
		//���o�Ҧ����I���y��
		MyVector3f[] va = this.getAllCorners();
		//�Ω�s���g�ܴ��᪺���I�}�C
	    float[] transformedCorners=new float[24];
	    //�N�ܴ��e��AABB�]�򲰪�8�ӳ��I�P��g�ܴ��x�}m�ۭ��A�o���g�ܴ��᪺OBB�]�򲰪��Ҧ����I
		float[] tmpResult=new float[4];
	    int count=0;
		for(int i=0;i<va.length;i++){
			float[] point=new float[]{va[i].x,va[i].y,va[i].z,1};//�N���I�ഫ�������y��
			Matrix.multiplyMV(tmpResult, 0, m, 0, point, 0);
			transformedCorners[count++]=tmpResult[0];
			transformedCorners[count++]=tmpResult[1];
			transformedCorners[count++]=tmpResult[2];
		}
		//�z�L�غc���NOBB�]���ഫ��AABB�]�򲰡A�öǦ^
		return new AABB3(transformedCorners);
	}
	public float getXSize(){//���ox��V�j�p
		return max.x - min.x;
	}
	public float getYSize(){//���oy��V�j�p
		return max.y - min.y;
	}
	public float getZSize(){//���oz��V�j�p
		return max.z - min.z;
	}
	public MyVector3f getSize(){//���o�﨤�u�V�q
		return max.minus(min);
	}
	//���o�]�򲰪������I�y�Ъ���k
	public MyVector3f getCenter(){
		return (min.add(max)).multiK(0.5f);
	}
	public float rayIntersect(
			MyVector3f rayStart,//�g�u�_�I
			MyVector3f rayDir,//�g�u���שM��V
			MyVector3f returnNormal//�i�諸�A�ۥ��I�B�k�V�q
			){
		//�Y�G���ۥ�h�Ǧ^�o�Ӥj��
		final float kNoIntersection = Float.POSITIVE_INFINITY;
		//�ˬd�I�b�x����ɤ������p�A�íp���C�ӭ����Z��
		boolean inside = true;
		float xt, xn = 0.0f;
		if(rayStart.x<min.x){
			xt = min.x - rayStart.x;
			if(xt>rayDir.x){ return kNoIntersection; }
			xt /= rayDir.x;
			inside = false;
			xn = -1.0f;
		}
		else if(rayStart.x>max.x){
			xt = max.x - rayStart.x;
			if(xt<rayDir.x){ return kNoIntersection; }
			xt /= rayDir.x;
			inside = false;
			xn = 1.0f;
		}
		else{
			xt = -1.0f;
		}
		
		float yt, yn = 0.0f;
		if(rayStart.y<min.y){
			yt = min.y - rayStart.y;
			if(yt>rayDir.y){ return kNoIntersection; }
			yt /= rayDir.y;
			inside = false;
			yn = -1.0f;
		}
		else if(rayStart.y>max.y){
			yt = max.y - rayStart.y;
			if(yt<rayDir.y){ return kNoIntersection; }
			yt /= rayDir.y;
			inside = false;
			yn = 1.0f;
		}
		else{
			yt = -1.0f;
		}
		
		float zt, zn = 0.0f;
		if(rayStart.z<min.z){
			zt = min.z - rayStart.z;
			if(zt>rayDir.z){ return kNoIntersection; }
			zt /= rayDir.z;
			inside = false;
			zn = -1.0f;
		}
		else if(rayStart.z>max.z){
			zt = max.z - rayStart.z;
			if(zt<rayDir.z){ return kNoIntersection; }
			zt /= rayDir.z;
			inside = false;
			zn = 1.0f;
		}
		else{
			zt = -1.0f;
		}
		//�O�_�b�x����ɮؤ��H
		if(inside){
			if(returnNormal != null){
				returnNormal = rayDir.multiK(-1);
				returnNormal.normalize();
			}
			return 0.0f;
		}
		//����̻��������X�X�X�X�o�ͬۥ檺�a��
		int which = 0;
		float t = xt;
		if(yt>t){
			which = 1;
			t=yt;
		}
		if(zt>t){
			which = 2;
			t=zt;
		}
		switch(which){
			case 0://�Myz�����ۥ�
			{
				float y=rayStart.y+rayDir.y*t;
				if(y<min.y||y>max.y){return kNoIntersection;}
				float z=rayStart.z+rayDir.z*t;
				if(z<min.z||z>max.z){return kNoIntersection;}
				if(returnNormal != null){
					returnNormal.x = xn;
					returnNormal.y = 0.0f;
					returnNormal.z = 0.0f;
				}				
			}
			break;
			case 1://�Mxz�����ۥ�
			{
				float x=rayStart.x+rayDir.x*t;
				if(x<min.x||x>max.x){return kNoIntersection;}
				float z=rayStart.z+rayDir.z*t;
				if(z<min.z||z>max.z){return kNoIntersection;}
				if(returnNormal != null){
					returnNormal.x = 0.0f;
					returnNormal.y = yn;
					returnNormal.z = 0.0f;
				}				
			}
			break;
			case 2://�Mxy�����ۥ�
			{
				float x=rayStart.x+rayDir.x*t;
				if(x<min.x||x>max.x){return kNoIntersection;}
				float y=rayStart.y+rayDir.y*t;
				if(y<min.y||y>max.y){return kNoIntersection;}
				if(returnNormal != null){
					returnNormal.x = 0.0f;
					returnNormal.y = 0.0f;
					returnNormal.z = zn;
				}				
			}
			break;
		}
		return t;//�Ǧ^�ۥ��I�Ѽƭ�
	}
}
