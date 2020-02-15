package com.f.util;



import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.Matrix;

public final class MatrixState {

	private static float[] currMatrix = null ;//�ثe�ܴ��x�}
	private static float[] mProjMatrix = new float[16];//4x4�x�} ��v��
    private static float[] mCameraMatrix = new float[16];//��v����m�¦V9�ѼƯx�}   

    
    private  static FloatBuffer lightPositionBuffer = null;//�w�����m��ƽw�R��
    private  static FloatBuffer lightDirectionBuffer = null;//�w�V�O��m��ƽw�R��
    private  static FloatBuffer cameraBuffer = null;//��v����m��ƽw�R��
    
    
    private static float[][] mStack=new float[10][16];
    private static int stackTop=-1;
    /**
     * �_�l�ƥثe�x�}
     */
    public static void setInitStack()
    {
    	currMatrix=new float[16];
    	Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
    }
    /**
     * �N�ثe�x�}�����|
     */
    public static void pushMatrix()
    {
    	stackTop++;
    	for(int i=0;i<16;i++)
    	{
    		mStack[stackTop][i]=currMatrix[i];
    	}
    }
    /**
     * �N�ثe�x�}�X���|
     */
    public static void popMatrix()
    {
    	for(int i=0;i<16;i++)
    	{
    		currMatrix[i]=mStack[stackTop][i];
    	}
    	stackTop--;
    }
    /**
     * �N�ثe�x�}�uxyz��V�h��
     * @param x 
     * @param y
     * @param z
     */
    public static void translate(float x,float y,float z)
    {
    	Matrix.translateM(currMatrix, 0, x, y, z);
    }
    /**
     * �N�ثe�x�}�uxyz��V����angle��
     * @param angle
     * @param x
     * @param y
     * @param z
     */
    public static void rotate(float angle,float x,float y,float z)
    {
    	Matrix.rotateM(currMatrix,0,angle,x,y,z);
    }
    
    /**
    * �N�ثe�x�}�Y��currMatrix
    */
   public static void scale(float x,float y,float z){
   	Matrix.scaleM(currMatrix, 0, x, y, z);
   }
    
    /**
     * �]�w��v������m�A�ت��I�Aup�V�q�A�Y�γo�Ǹ�ƫغc�@����v���x�}
     * @param cx
     * @param cy
     * @param cz
     * @param tx
     * @param ty
     * @param tz
     * @param upx
     * @param upy
     * @param upz
     */
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
            		mCameraMatrix, 
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
        	/**
        	 * �N��v����m��Ʃ�J�w�R
        	 */
        	ByteBuffer llbb= ByteBuffer.allocateDirect(3*4);
            float[] cameraLocation={cx,cy,cz};//��v����m
        	llbb.clear();
            llbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
            cameraBuffer=llbb.asFloatBuffer();
            cameraBuffer.put(cameraLocation);
            cameraBuffer.position(0);  
    }
    /**
     * �]�w�z����v�ѼơA�Y�γo�Ǹ�ƫغc�@�ӳz����v�x�}
     * �ϥΦ���Ƴ]�w�z����v�x�}�ɡA���T�Oleft�Pbottom����һP��������Ҥ@�P�C
     * 1.�p����������e��  float ratio = (float) width / height;
     * 2. - left = right �A - bottom = top
     * 3. �ϱoright/top = ratio�C
     * e.g. �G
     * float ratio = (float) width / height;
     * MatrixState.setProjectFrustum(-x*ratio, x*ratio, -x, x, zNear, zFar);
     * x:�y���Y��]�l
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
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
    
    /**
     * �]�w�����v�ѼơA�Y�γo�Ǹ�ƫغc�@�ӥ����v�x�}
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
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
    
    /**
     * �ثe�x�}x��v���x�}x��v�x�}(�����v�x�}�γz����v�x�}) = �`�ܴ��x�}
     * @return ���骺�`�ܴ��x�}
     */
    public static float[] getFinalMatrix()
    {	
    	float[] mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mCameraMatrix, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);        
        return mMVPMatrix;
    }
    
    /**
     * ��v�B��v���s�զX�x�}
     * �p�⥭�����v�Ψ�
     */
    public static float[] getViewProjMatrix()
    {
    	float[] mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mCameraMatrix, 0);      
        return mMVPMatrix;
    }
    
    /**
     * @return ���骺�ثe���ҫ��˵��x�}�A�Y�ܴ��x�}
     */
    public static float[] getMMatrix()
    {       
        return currMatrix;
    }

    /**
     * �]�w�w����O����m�A�ñN��y�Щ�JlightPositionBuffer�w�R�A���ݶǤJ�ۦ⾹
     * @param x
     * @param y
     * @param z
     */
    public static void setLightLocation(float x,float y,float z)
    {
    	float[] lightLocation={x,y,z};//�w���������m
    	ByteBuffer llbbL = ByteBuffer.allocateDirect(3*4);
    	llbbL.clear();
        llbbL.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        lightPositionBuffer=llbbL.asFloatBuffer();
        lightPositionBuffer.put(lightLocation);
        lightPositionBuffer.position(0);
    }
    /**
     * �]�w�w�V���O����m�A�ñN��y�Щ�JlightDirectionBuffer�w�R�A���ݶǤJ�ۦ⾹
     * @param x
     * @param y
     * @param z
     */
    public static void setLightDirection0(float x,float y,float z){
    	float lightDirection[] = {x,y,z};
    	ByteBuffer llbbL = ByteBuffer.allocateDirect(3*4);
    	llbbL.clear();
        llbbL.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        lightDirectionBuffer=llbbL.asFloatBuffer();
        lightDirectionBuffer.put(lightDirection);
        lightDirectionBuffer.position(0);
    }
    /**
     * @return �w����O����m����ƽw�R��
     */
    public static FloatBuffer getLightPositionBuffer(){
    	return lightPositionBuffer;
    }
    /**
     * @return �w�V���O����m����ƽw�R��
     */
    public static FloatBuffer getLightDirectionBuffer(){
    	return lightDirectionBuffer;
    }
    /**
     * @return ��v����m����ƽw�R��
     */
    public static FloatBuffer getCameraPosBuffer(){
    	return cameraBuffer;
    }
}
