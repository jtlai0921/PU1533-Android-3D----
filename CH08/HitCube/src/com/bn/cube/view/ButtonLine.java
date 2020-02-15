package com.bn.cube.view;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.bn.cube.core.MatrixState;
import com.bn.cube.core.ShaderUtil;

import android.opengl.GLES20;

public class ButtonLine 
{
	int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;  
    int maColorHandle; 
    int muMMatrixHandle;//基本變換矩陣參考id
    int muRHandle;//半徑
    int muCountHandle;
    String mVertexShader;
    String mFragmentShader;
    public float count=1;
    static float[] mMMatrix = new float[16];
	
	public FloatBuffer   mVertexBuffer;
    public FloatBuffer   mColorBuffer;
    public  int vCount=0;    
    float yAngle=0;
    float xAngle=0;
    float color[];
    public ButtonLine(MyGLSurfaceView mv,float r,float R,float z,float color[])
    {    
    	this.color=color;
    	initVertexData(R,r,z);   
    	initShader(mv);
    }
    

    public void initVertexData(float R,float r,float z)
    {
		List<Float> flist=new ArrayList<Float>();
		float tempAngle=360/6;
		for(float angle=0;angle<360;angle+=tempAngle)
		{
			flist.add((float) (r*Constant.UNIT_SIZE*Math.cos(Math.toRadians(angle))));
			flist.add((float) (r*Constant.UNIT_SIZE*Math.sin(Math.toRadians(angle))));
			flist.add(0f);
			flist.add((float) (R*Constant.UNIT_SIZE*Math.cos(Math.toRadians(angle))));
			flist.add((float) (R*Constant.UNIT_SIZE*Math.sin(Math.toRadians(angle))));
			flist.add(0f);
			flist.add((float) (R*Constant.UNIT_SIZE*Math.cos(Math.toRadians(angle+tempAngle))));
			flist.add((float) (R*Constant.UNIT_SIZE*Math.sin(Math.toRadians(angle+tempAngle))));
			flist.add(0f);			
			flist.add((float) (r*Constant.UNIT_SIZE*Math.cos(Math.toRadians(angle))));
			flist.add((float) (r*Constant.UNIT_SIZE*Math.sin(Math.toRadians(angle))));
			flist.add(0f);
			flist.add((float) (R*Constant.UNIT_SIZE*Math.cos(Math.toRadians(angle+tempAngle))));
			flist.add((float) (R*Constant.UNIT_SIZE*Math.sin(Math.toRadians(angle+tempAngle))));
			flist.add(0f);
			flist.add((float) (r*Constant.UNIT_SIZE*Math.cos(Math.toRadians(angle+tempAngle))));
			flist.add((float) (r*Constant.UNIT_SIZE*Math.sin(Math.toRadians(angle+tempAngle))));
			flist.add(0f);
		}
		vCount=flist.size()/3;
		float[] vertexArray=new float[flist.size()];
		for(int i=0;i<vCount;i++)
		{
			vertexArray[i*3]=flist.get(i*3);
			vertexArray[i*3+1]=flist.get(i*3+1);
			vertexArray[i*3+2]=flist.get(i*3+2);
		}
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertexArray.length*4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer=vbb.asFloatBuffer();
		mVertexBuffer.put(vertexArray);
		mVertexBuffer.position(0);

		float[] colorArray=new float[vCount*4];
		for(int i=0;i<vCount;i++)
		{			
				colorArray[i*4]=0;
				colorArray[i*4+1]=0;
				colorArray[i*4+2]=1;
				colorArray[i*4+3]=0;

		}
		ByteBuffer cbb=ByteBuffer.allocateDirect(colorArray.length*4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer=cbb.asFloatBuffer();
		mColorBuffer.put(colorArray);
		mColorBuffer.position(0);

    }

    //自訂起始化著色器的intShader方法
    public void initShader(MyGLSurfaceView mv)
    {

        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_cube.sh", mv.getResources());

        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_cube.sh", mv.getResources());  

        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
   
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
 
        maColorHandle= GLES20.glGetAttribLocation(mProgram, "aColor");

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix"); 
        muRHandle = GLES20.glGetUniformLocation(mProgram, "uR"); 
        muCountHandle= GLES20.glGetUniformLocation(mProgram, "uCount"); 
    }
    
    public void drawSelf()
    {    

    	 GLES20.glUseProgram(mProgram);        
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
         GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0); 
         GLES20.glUniform1f(muRHandle, 0.48f);
         GLES20.glUniform1f(muCountHandle,count); 
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4, 
                mVertexBuffer
         );       
         GLES20.glVertexAttribPointer  
         (
        		maColorHandle, 
         		4, 
         		GLES20.GL_FLOAT, 
         		false,
                4*4,   
                mColorBuffer
         );   
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maColorHandle);  
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}


