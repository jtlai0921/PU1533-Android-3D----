package com.cw.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Stack;
import android.opengl.Matrix;

//�x�s�t�ίx�}���A�����O
public class MatrixState 
{
	private static float[] mProjMatrix = new float[16];//4x4�x�} ��v��
    static float[] mVMatrix = new float[16];//��v����m�¦V9�ѼƯx�}
    static float[] mMVPMatrix;//�̫�_�@�Ϊ��`�ܴ��x�}
    public static float[] lightLocationRed=new float[]{0,0,0};//����w���������m
    public static float[] lightLocationGreenBlue=new float[]{0,0,0};//���Ŧ�w���������m
    public static FloatBuffer cameraFB;
    public static FloatBuffer lightPositionFBRed;
    public static FloatBuffer lightPositionFBGreenBlue;
    public static float[] lightLocation=new float[]{0,0,0};//�w���������m
    public static FloatBuffer lightPositionFB;
    //�]�w��v��
    public static void setCamera
    (
    		float cx,	//��v����mx
    		float cy,   //��v����my
    		float cz,   //��v����mz
    		float tx,   //��v���ت��Ix
    		float ty,   //��v���ت��Iy
    		float tz,   //��v���ت��Iz
    		float upx,  //��v��UP�V�qX���q
    		float upy,  //��v��UP�V�qY���q
    		float upz   //��v��UP�V�qZ���q		
    )
    {
    	Matrix.setLookAtM
        (
        		mVMatrix, 
        		0, 
        		cx,
        		cy,
        		cz,
        		tx,
        		ty,
        		tz,
        		upx,
        		upy,
        		upz
        );
    	
    	float[] cameraLocation=new float[3];//��v����m
    	cameraLocation[0]=cx;
    	cameraLocation[1]=cy;
    	cameraLocation[2]=cz;
    	//��v����m�x�}
    	ByteBuffer llbb = ByteBuffer.allocateDirect(3*4);
        llbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        cameraFB=llbb.asFloatBuffer();
        cameraFB.put(cameraLocation);
        cameraFB.position(0);  
    }
    
    //�]�w�z����v�Ѽ�
    public static void setProjectFrustum
    (
    	float left,		//near����left
    	float right,    //near����right
    	float bottom,   //near����bottom
    	float top,      //near����top
    	float near,		//near���Z��
    	float far       //far���Z��
    )
    {
    	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);    	
    }
    
    //�]�w�����v�Ѽ�
    public static void setProjectOrtho
    (
    	float left,		//near����left
    	float right,    //near����right
    	float bottom,   //near����bottom
    	float top,      //near����top
    	float near,		//near���Z��
    	float far       //far���Z��
    )
    {    	
    	Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }   
    private static float[] mVMatrixForSpecFrame = new float[16];//��v����m�¦V9�ѼƯx�}   
    public static void copyMVMatrix()
    {
		for(int i=0;i<16;i++)
    	{
    		mVMatrixForSpecFrame[i]=mVMatrix[i];        		
    	}
    }
    //���o��ڪ��骺�`�ܴ��x�}
    public static float[] getFinalMatrix()
    {
    	float[] mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mVMatrixForSpecFrame, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);        
        return mMVPMatrix;
    }
    
    //�]�w����O����m����k
    public static void setLightLocationRed(float x,float y,float z)
    {
    	lightLocationRed[0]=x;
    	lightLocationRed[1]=y;
    	lightLocationRed[2]=z;
    	ByteBuffer llbb = ByteBuffer.allocateDirect(3*4);
        llbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        lightPositionFBRed=llbb.asFloatBuffer();
        lightPositionFBRed.put(lightLocationRed);
        lightPositionFBRed.position(0);
    }
    
    //�]�w���Ŧ�O����m����k
    public static void setLightLocationGreenBlue(float x,float y,float z)
    {
    	lightLocationGreenBlue[0]=x;
    	lightLocationGreenBlue[1]=y;
    	lightLocationGreenBlue[2]=z;
    	ByteBuffer llbb = ByteBuffer.allocateDirect(3*4);
        llbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        lightPositionFBGreenBlue=llbb.asFloatBuffer();
        lightPositionFBGreenBlue.put(lightLocationGreenBlue);
        lightPositionFBGreenBlue.position(0);
    }
    
    public static Stack<float[]> mStack=new Stack<float[]>();//�O�@�ܴ��x�}�����|
    static float[] currMatrix;//�ثe�ܴ��x�}
 
    public static void setInitStack()//���o���ܴ��_�l�x�}
    {
    	currMatrix=new float[16];
    	Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
    }
    
    public static void pushMatrix()//�O�@�ܴ��x�}
    {
    	mStack.push(currMatrix.clone());
    }
    
    public static void popMatrix()//�٭��ܴ��x�}
    {
    	currMatrix=mStack.pop();
    }
    
    public static void translate(float x,float y,float z)//�]�w�uxyz�b�h��
    {
    	Matrix.translateM(currMatrix, 0, x, y, z);
    }
    
    public static void rotate(float angle,float x,float y,float z)//�]�w¶xyz�b�h��
    {
    	Matrix.rotateM(currMatrix,0,angle,x,y,z);
    }
    
    public static void scale(float x,float y,float z){
    	Matrix.scaleM(currMatrix, 0, x, y, z);
    }
    
    //���J���a�x�}
    public static void matrix(float[] self)
    {
    	float[] result=new float[16];
    	Matrix.multiplyMM(result,0,currMatrix,0,self,0);
    	currMatrix=result;
    }

	public static float[] getMMatrix() {
		return currMatrix;
	}
	
	
	public static float[] getInvertMvMatrix(){
		float[] invM = new float[16];
		Matrix.invertM(invM, 0, mVMatrix, 0);//�D�f�x�}
		return invM;
	}
	//�z�L��v���ܴ��᪺�I�D�ܴ��e���I����k�G���H��v���x�}���f�x�}
	public static float[] fromPtoPreP(float[] p){
		//�z�L�f�ܴ��A�o���ܴ����e���I
		float[] inverM = getInvertMvMatrix();//���o�f�ܴ��x�}
		float[] preP = new float[4];
		Matrix.multiplyMV(preP, 0, inverM, 0, new float[]{p[0],p[1],p[2],1}, 0);//�D�ܴ��e���I
		return new float[]{preP[0],preP[1],preP[2]};//�ܴ��e���I�N�O�ܴ����e���k�V�q
	}
    public static void setLightLocation(float x,float y,float z)
    {
    	lightLocation[0]=x;
    	lightLocation[1]=y;
    	lightLocation[2]=z;
    	ByteBuffer llbb = ByteBuffer.allocateDirect(3*4);
        llbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        lightPositionFB=llbb.asFloatBuffer();
        lightPositionFB.put(lightLocation);
        lightPositionFB.position(0);
    }
    
    public static float[] getViewProjMatrix()
    {
    	float[] mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);      
        return mMVPMatrix;
    }
}
