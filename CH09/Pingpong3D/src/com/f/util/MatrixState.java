package com.f.util;



import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.Matrix;

public final class MatrixState {

	private static float[] currMatrix = null ;//目前變換矩陣
	private static float[] mProjMatrix = new float[16];//4x4矩陣 投影用
    private static float[] mCameraMatrix = new float[16];//攝影機位置朝向9參數矩陣   

    
    private  static FloatBuffer lightPositionBuffer = null;//定位光位置資料緩沖區
    private  static FloatBuffer lightDirectionBuffer = null;//定向燈位置資料緩沖區
    private  static FloatBuffer cameraBuffer = null;//攝影機位置資料緩沖區
    
    
    private static float[][] mStack=new float[10][16];
    private static int stackTop=-1;
    /**
     * 起始化目前矩陣
     */
    public static void setInitStack()
    {
    	currMatrix=new float[16];
    	Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
    }
    /**
     * 將目前矩陣壓堆疊
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
     * 將目前矩陣出堆疊
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
     * 將目前矩陣沿xyz方向搬移
     * @param x 
     * @param y
     * @param z
     */
    public static void translate(float x,float y,float z)
    {
    	Matrix.translateM(currMatrix, 0, x, y, z);
    }
    /**
     * 將目前矩陣沿xyz方向旋轉angle度
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
    * 將目前矩陣縮放currMatrix
    */
   public static void scale(float x,float y,float z){
   	Matrix.scaleM(currMatrix, 0, x, y, z);
   }
    
    /**
     * 設定攝影機的位置，目的點，up向量，即用這些資料建構一個攝影機矩陣
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
        	 * 將攝影機位置資料放入緩沖
        	 */
        	ByteBuffer llbb= ByteBuffer.allocateDirect(3*4);
            float[] cameraLocation={cx,cy,cz};//攝影機位置
        	llbb.clear();
            llbb.order(ByteOrder.nativeOrder());//設定位元組順序
            cameraBuffer=llbb.asFloatBuffer();
            cameraBuffer.put(cameraLocation);
            cameraBuffer.position(0);  
    }
    /**
     * 設定透視投影參數，即用這些資料建構一個透視投影矩陣
     * 使用此函數設定透視投影矩陣時，應確保left與bottom的比例與視窗的比例一致。
     * 1.計算視窗的長寬比  float ratio = (float) width / height;
     * 2. - left = right ， - bottom = top
     * 3. 使得right/top = ratio。
     * e.g. ：
     * float ratio = (float) width / height;
     * MatrixState.setProjectFrustum(-x*ratio, x*ratio, -x, x, zNear, zFar);
     * x:座標縮放因子
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
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
    
    /**
     * 設定正交投影參數，即用這些資料建構一個正交投影矩陣
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
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
    
    /**
     * 目前矩陣x攝影機矩陣x投影矩陣(正交投影矩陣或透視投影矩陣) = 總變換矩陣
     * @return 物體的總變換矩陣
     */
    public static float[] getFinalMatrix()
    {	
    	float[] mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mCameraMatrix, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);        
        return mMVPMatrix;
    }
    
    /**
     * 投影、攝影機群組合矩陣
     * 計算平面陰影用到
     */
    public static float[] getViewProjMatrix()
    {
    	float[] mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mCameraMatrix, 0);      
        return mMVPMatrix;
    }
    
    /**
     * @return 物體的目前的模型檢視矩陣，即變換矩陣
     */
    public static float[] getMMatrix()
    {       
        return currMatrix;
    }

    /**
     * 設定定位光燈光位置，並將其座標放入lightPositionBuffer緩沖，等待傳入著色器
     * @param x
     * @param y
     * @param z
     */
    public static void setLightLocation(float x,float y,float z)
    {
    	float[] lightLocation={x,y,z};//定位光光源位置
    	ByteBuffer llbbL = ByteBuffer.allocateDirect(3*4);
    	llbbL.clear();
        llbbL.order(ByteOrder.nativeOrder());//設定位元組順序
        lightPositionBuffer=llbbL.asFloatBuffer();
        lightPositionBuffer.put(lightLocation);
        lightPositionBuffer.position(0);
    }
    /**
     * 設定定向光燈光位置，並將其座標放入lightDirectionBuffer緩沖，等待傳入著色器
     * @param x
     * @param y
     * @param z
     */
    public static void setLightDirection0(float x,float y,float z){
    	float lightDirection[] = {x,y,z};
    	ByteBuffer llbbL = ByteBuffer.allocateDirect(3*4);
    	llbbL.clear();
        llbbL.order(ByteOrder.nativeOrder());//設定位元組順序
        lightDirectionBuffer=llbbL.asFloatBuffer();
        lightDirectionBuffer.put(lightDirection);
        lightDirectionBuffer.position(0);
    }
    /**
     * @return 定位光燈光位置的資料緩沖區
     */
    public static FloatBuffer getLightPositionBuffer(){
    	return lightPositionBuffer;
    }
    /**
     * @return 定向光燈光位置的資料緩沖區
     */
    public static FloatBuffer getLightDirectionBuffer(){
    	return lightDirectionBuffer;
    }
    /**
     * @return 攝影機位置的資料緩沖區
     */
    public static FloatBuffer getCameraPosBuffer(){
    	return cameraBuffer;
    }
}
