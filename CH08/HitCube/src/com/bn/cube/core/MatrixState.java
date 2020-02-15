package com.bn.cube.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.*;
import android.opengl.Matrix;

//�x�s�t�ίx�}���A�����O
public class MatrixState 
{
	private static float[] mProjMatrix = new float[16];//4x4�x�} ��v��
    private static float[] mVMatrix = new float[16];//��v����m�¦V9�ѼƯx�}   
    private static float[] currMatrix;//�ثe�ܴ��x�}
    public static float[] lightLocation=new float[]{0,0,0};//�w���������m
    public static FloatBuffer cameraFB;    
    public static FloatBuffer lightPositionFB;
    
    public static Stack<float[]> mStack=new Stack<float[]>();//�O�@�ܴ��x�}�����|
    
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
    public static void scale(float x,float y,float z)
    {
    	Matrix.scaleM(currMatrix,0, x, y, z);
    }
    
    
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
    
    
    private static float[] mVMatrixForSpecFrame = new float[16];//��v����m�¦V9�ѼƯx�}   
    public static void copyMVMatrix()
    {
		for(int i=0;i<16;i++)
    	{
    		mVMatrixForSpecFrame[i]=mVMatrix[i];        		
    	}
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
   
    //���o��ڪ��骺�`�ܴ��x�}
    public static float[] getFinalMatrix()
    {
    	float[] mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);        
        return mMVPMatrix;
    }
    
    //���o��ڪ��骺�ܴ��x�}
    public static float[] getMMatrix()
    {       
        return currMatrix;
    }
    
    //�]�w�O����m����k
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
}
