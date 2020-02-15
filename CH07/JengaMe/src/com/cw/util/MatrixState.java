package com.cw.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Stack;
import android.opengl.Matrix;

//儲存系統矩陣狀態的類別
public class MatrixState 
{
	private static float[] mProjMatrix = new float[16];//4x4矩陣 投影用
    static float[] mVMatrix = new float[16];//攝影機位置朝向9參數矩陣
    static float[] mMVPMatrix;//最後起作用的總變換矩陣
    public static float[] lightLocationRed=new float[]{0,0,0};//紅色定位光光源位置
    public static float[] lightLocationGreenBlue=new float[]{0,0,0};//天藍色定位光光源位置
    public static FloatBuffer cameraFB;
    public static FloatBuffer lightPositionFBRed;
    public static FloatBuffer lightPositionFBGreenBlue;
    public static float[] lightLocation=new float[]{0,0,0};//定位光光源位置
    public static FloatBuffer lightPositionFB;
    //設定攝影機
    public static void setCamera
    (
    		float cx,	//攝影機位置x
    		float cy,   //攝影機位置y
    		float cz,   //攝影機位置z
    		float tx,   //攝影機目的點x
    		float ty,   //攝影機目的點y
    		float tz,   //攝影機目的點z
    		float upx,  //攝影機UP向量X分量
    		float upy,  //攝影機UP向量Y分量
    		float upz   //攝影機UP向量Z分量		
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
    	
    	float[] cameraLocation=new float[3];//攝影機位置
    	cameraLocation[0]=cx;
    	cameraLocation[1]=cy;
    	cameraLocation[2]=cz;
    	//攝影機位置矩陣
    	ByteBuffer llbb = ByteBuffer.allocateDirect(3*4);
        llbb.order(ByteOrder.nativeOrder());//設定位元組順序
        cameraFB=llbb.asFloatBuffer();
        cameraFB.put(cameraLocation);
        cameraFB.position(0);  
    }
    
    //設定透視投影參數
    public static void setProjectFrustum
    (
    	float left,		//near面的left
    	float right,    //near面的right
    	float bottom,   //near面的bottom
    	float top,      //near面的top
    	float near,		//near面距離
    	float far       //far面距離
    )
    {
    	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);    	
    }
    
    //設定正交投影參數
    public static void setProjectOrtho
    (
    	float left,		//near面的left
    	float right,    //near面的right
    	float bottom,   //near面的bottom
    	float top,      //near面的top
    	float near,		//near面距離
    	float far       //far面距離
    )
    {    	
    	Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }   
    private static float[] mVMatrixForSpecFrame = new float[16];//攝影機位置朝向9參數矩陣   
    public static void copyMVMatrix()
    {
		for(int i=0;i<16;i++)
    	{
    		mVMatrixForSpecFrame[i]=mVMatrix[i];        		
    	}
    }
    //取得實際物體的總變換矩陣
    public static float[] getFinalMatrix()
    {
    	float[] mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mVMatrixForSpecFrame, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);        
        return mMVPMatrix;
    }
    
    //設定紅色燈光位置的方法
    public static void setLightLocationRed(float x,float y,float z)
    {
    	lightLocationRed[0]=x;
    	lightLocationRed[1]=y;
    	lightLocationRed[2]=z;
    	ByteBuffer llbb = ByteBuffer.allocateDirect(3*4);
        llbb.order(ByteOrder.nativeOrder());//設定位元組順序
        lightPositionFBRed=llbb.asFloatBuffer();
        lightPositionFBRed.put(lightLocationRed);
        lightPositionFBRed.position(0);
    }
    
    //設定天藍色燈光位置的方法
    public static void setLightLocationGreenBlue(float x,float y,float z)
    {
    	lightLocationGreenBlue[0]=x;
    	lightLocationGreenBlue[1]=y;
    	lightLocationGreenBlue[2]=z;
    	ByteBuffer llbb = ByteBuffer.allocateDirect(3*4);
        llbb.order(ByteOrder.nativeOrder());//設定位元組順序
        lightPositionFBGreenBlue=llbb.asFloatBuffer();
        lightPositionFBGreenBlue.put(lightLocationGreenBlue);
        lightPositionFBGreenBlue.position(0);
    }
    
    public static Stack<float[]> mStack=new Stack<float[]>();//保護變換矩陣的堆疊
    static float[] currMatrix;//目前變換矩陣
 
    public static void setInitStack()//取得不變換起始矩陣
    {
    	currMatrix=new float[16];
    	Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
    }
    
    public static void pushMatrix()//保護變換矩陣
    {
    	mStack.push(currMatrix.clone());
    }
    
    public static void popMatrix()//還原變換矩陣
    {
    	currMatrix=mStack.pop();
    }
    
    public static void translate(float x,float y,float z)//設定沿xyz軸搬移
    {
    	Matrix.translateM(currMatrix, 0, x, y, z);
    }
    
    public static void rotate(float angle,float x,float y,float z)//設定繞xyz軸搬移
    {
    	Matrix.rotateM(currMatrix,0,angle,x,y,z);
    }
    
    public static void scale(float x,float y,float z){
    	Matrix.scaleM(currMatrix, 0, x, y, z);
    }
    
    //插入附帶矩陣
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
		Matrix.invertM(invM, 0, mVMatrix, 0);//求逆矩陣
		return invM;
	}
	//透過攝影機變換後的點求變換前的點的方法：乘以攝影機矩陣的逆矩陣
	public static float[] fromPtoPreP(float[] p){
		//透過逆變換，得到變換之前的點
		float[] inverM = getInvertMvMatrix();//取得逆變換矩陣
		float[] preP = new float[4];
		Matrix.multiplyMV(preP, 0, inverM, 0, new float[]{p[0],p[1],p[2],1}, 0);//求變換前的點
		return new float[]{preP[0],preP[1],preP[2]};//變換前的點就是變換之前的法向量
	}
    public static void setLightLocation(float x,float y,float z)
    {
    	lightLocation[0]=x;
    	lightLocation[1]=y;
    	lightLocation[2]=z;
    	ByteBuffer llbb = ByteBuffer.allocateDirect(3*4);
        llbb.order(ByteOrder.nativeOrder());//設定位元組順序
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
