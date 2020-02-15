package com.f.mainbody;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;
import android.opengl.GLES20;

import com.f.pingpong.Constant;
import com.f.util.From2DTo3DUtil;
import com.f.util.MatrixState;
import com.f.util.ShaderUtil;
import static com.f.pingpong.Constant.*;
import static com.f.pingpong.ShaderManager.*;

public class MainMenuRect 
{
	private int vCount;
	private int mProgram;
	private int muMVPMatrixHandle;
	private int maPositionHandle; 
	private int maTexCoorHandle;
	private FloatBuffer mVertexBuffer = null;
	private FloatBuffer mTexCoorBuffer = null;
	
	public MainMenuRect(Resources res,float width,float height)
	{
		initVertexData(width,height);
		initShader(res);
	}

	public MainMenuRect(FloatBuffer mVertexBuffer,FloatBuffer mTexCoorBuffer)
	{
		this.mVertexBuffer = mVertexBuffer;
		this.mTexCoorBuffer = mTexCoorBuffer;
		initShader();
	}
	
	private void initVertexData(float width,float height) 
	{
		float vertices[] = From2DTo3DUtil.Vertices(width,height);
    	this.vCount = vertices.length/3;
    	ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//�إ߳��I��ƽw�R
    	vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
    	mVertexBuffer = vbb.asFloatBuffer();//�ରint���w�R
    	mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        
        float textureCoors[] = new float[]
    	{
        		0.0f, 0.0f,
    			0.0f, 1.0f, 
    			1.0f, 0.0f,
    			
    			1.0f, 0.0f, 
    			0.0f, 1.0f, 
    			1.0f, 1.0f
    	};
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length*4);//�إ߳��I���z��ƽw�R
        tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTexCoorBuffer= tbb.asFloatBuffer();//�ରFloat���w�R
        mTexCoorBuffer.put(textureCoors);//�V�w�R�Ϥ���J���I�ۦ���
        mTexCoorBuffer.position(0);//�]�w�w�R�ϰ_�l��m
	}
	
	private void initShader(Resources res)
	{
		String mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_spring.sh",res);
		String mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_spring.sh",res);
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}
	
	private void initShader()
	{
		mProgram = program[3];
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}
	
	public void drawSelf(float[] position,int texId)
	{
		MatrixState.pushMatrix();
		MatrixState.translate(position[0], position[1], -1f);
		GLES20.glUseProgram(mProgram);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,MatrixState.getFinalMatrix(), 0);
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,false, 3 * 4, mVertexBuffer);
		GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT,false, 2 * 4, mTexCoorBuffer);
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, IS_FIRSTLOADING_MAIN ? vCount : Constant.vCount[0][0]);
		MatrixState.popMatrix();
	}
}
