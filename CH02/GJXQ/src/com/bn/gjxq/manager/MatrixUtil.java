package com.bn.gjxq.manager;
import android.opengl.Matrix;

//儲存系統矩陣狀態的類別
public class MatrixUtil 
{
	//攝影機位置朝向9參數矩陣  
    public static float[] mVMatrix = new float[16]; 
    //目前變換矩陣
    public static float[] currMatrix;
    //取得不變換起始變換矩陣
    public static void setInitStack()
    {
    	currMatrix=new float[16];
    	Matrix.setIdentityM(currMatrix, 0);
    } 
    //將平移變換記錄進矩陣
    public static void translate(float x,float y,float z)
    {
    	Matrix.translateM(currMatrix, 0, x, y, z);
    }
    //將旋轉變換記錄進矩陣
    public static void rotate(float angle,float x,float y,float z)
    {
    	Matrix.rotateM(currMatrix,0,angle,x,y,z);
    }    
    
    //產生攝影機觀察矩陣
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
    }
	
	//求經指定變換矩陣逆矩陣還原的點座標
	public static Vector3f fromGToO(Vector3f v,float[] m)
	{
		float[] invM = new float[16];
		Matrix.invertM(invM, 0, m, 0);//求逆矩陣
		float[] preP = new float[4];
		Matrix.multiplyMV(preP, 0, invM, 0, new float[]{v.x,v.y,v.z,1}, 0);
		return new Vector3f(preP[0],preP[1],preP[2]);
	}
}
