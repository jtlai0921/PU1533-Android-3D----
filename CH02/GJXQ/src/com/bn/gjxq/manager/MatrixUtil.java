package com.bn.gjxq.manager;
import android.opengl.Matrix;

//�x�s�t�ίx�}���A�����O
public class MatrixUtil 
{
	//��v����m�¦V9�ѼƯx�}  
    public static float[] mVMatrix = new float[16]; 
    //�ثe�ܴ��x�}
    public static float[] currMatrix;
    //���o���ܴ��_�l�ܴ��x�}
    public static void setInitStack()
    {
    	currMatrix=new float[16];
    	Matrix.setIdentityM(currMatrix, 0);
    } 
    //�N�����ܴ��O���i�x�}
    public static void translate(float x,float y,float z)
    {
    	Matrix.translateM(currMatrix, 0, x, y, z);
    }
    //�N�����ܴ��O���i�x�}
    public static void rotate(float angle,float x,float y,float z)
    {
    	Matrix.rotateM(currMatrix,0,angle,x,y,z);
    }    
    
    //������v���[��x�}
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
    }
	
	//�D�g���w�ܴ��x�}�f�x�}�٭쪺�I�y��
	public static Vector3f fromGToO(Vector3f v,float[] m)
	{
		float[] invM = new float[16];
		Matrix.invertM(invM, 0, m, 0);//�D�f�x�}
		float[] preP = new float[4];
		Matrix.multiplyMV(preP, 0, invM, 0, new float[]{v.x,v.y,v.z,1}, 0);
		return new Vector3f(preP[0],preP[1],preP[2]);
	}
}
